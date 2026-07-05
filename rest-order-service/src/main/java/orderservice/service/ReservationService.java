package orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import orderservice.config.RabbitConfig;
import orderservice.entity.ReservationRequestDTO;
import orderservice.entity.ReservationResponse;
import orderservice.entity.ReservationStatus;
import orderservice.entity.RestaurantTableEntity;
import orderservice.entity.TableAvailabilityRequest;
import orderservice.repository.ReservationRepository;

@Service
public class ReservationService {

  private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

  private static final int BOOKING_START_HOUR = 11;
  private static final int BOOKING_END_HOUR = 22;
  private static final int MAX_PARTY_SIZE = 6;

  private ReservationRepository reservationRepository;

  private final RestTemplate restTemplate;


  @Autowired
  private RabbitTemplate rabbitTemplate;

  public ReservationService(ReservationRepository reservationRepository,
      RestTemplate restTemplate) {
    super();
    this.reservationRepository = reservationRepository;
    this.restTemplate = restTemplate;
  }



  public void validateBooking(String customerId, LocalDate date, LocalTime time) {
    // 1. Prevent Double Bookings (Same customer, same time, same day)
    boolean hasDoubleBooking = reservationRepository
        .existsByCustomerIdAndReservationDateAndReservationTime(customerId, date, time);
    if (hasDoubleBooking) {
      throw new IllegalStateException("Customer already has a booking at this time.");
    }
  }

  /**
   * Business Rule 1: Auto-cancel pending reservations after 1 hour Business Rule 2: Validate
   * booking window (11:00–22:00) Done Business Rule 3: Must be at least 30 minutes in the future
   * Done Business Rule 4: Party size must not exceed table capacity (≤ 6) Done Business Rule 5: No
   * overlapping reservations by same customer within an hour
   */


  public ReservationResponse createReservation(ReservationRequestDTO request) {
    ZoneId zone = ZoneId.of("America/Chicago");
    ZonedDateTime reservationTime =
        LocalDateTime.of(request.getDate(), request.getTime()).atZone(zone);
    ZonedDateTime now = ZonedDateTime.now(zone);

    logger.info("Reservation Time: {}", reservationTime);
    logger.info("Current Time + 30min: {}", now.plusMinutes(30));

    // 📌 Business Rule 3: Must be at least 30 minutes in the future
    if (reservationTime.isBefore(now.plusMinutes(30))) {
      throw new RuntimeException("Reservations must be at least 30 minutes in the future.");
    }

    // 📌 Business Rule 4: Party size must not exceed table capacity (≤ 6)
    if (request.getPartySize() > MAX_PARTY_SIZE) {
      logger.info("Party size > 6 .. {}", request.getPartySize());
      throw new RuntimeException("Maximum allowed party size per table is 6.");
    }

    // 📌 Business Rule 2: Validate booking window (11:00–22:00)
    LocalTime reservationLocalTime = reservationTime.toLocalTime();
    LocalTime startTime = LocalTime.of(BOOKING_START_HOUR, 0); // 11:00
    LocalTime endTime = LocalTime.of(BOOKING_END_HOUR, 0); // 22:00

    if (reservationLocalTime.isBefore(startTime) || reservationLocalTime.isAfter(endTime)) {
      String errMsg = String.format(
          "Booking Rejected: Outside operational hours. [Requested: %s], [Allowed: %d:00 - %d:00]",
          reservationLocalTime, BOOKING_START_HOUR, BOOKING_END_HOUR);
      logger.info(errMsg);
      throw new RuntimeException(errMsg);
    }

    // 📌 Business Rule 5: No overlapping reservations by same customer within an hour
    validateBooking(request.getCustomerId(), request.getDate(), request.getTime());

    // Build Table Availability Request for the remote Table Microservice
    TableAvailabilityRequest availabilityRequest = new TableAvailabilityRequest();
    availabilityRequest.setCustomerId(request.getCustomerId());
    availabilityRequest.setDate(request.getDate().toString());
    availabilityRequest.setTime(request.getTime().toString());
    availabilityRequest.setPartySize(request.getPartySize());

    logger.info("Sending availability check payload via HTTP: \n{}", availabilityRequest);

    // Call Table Service via RestTemplate HTTP POST (Synchronous verification)
    ResponseEntity<ReservationResponse> response =
        restTemplate.postForEntity("http://localhost:1987/api/tables/availability",
            availabilityRequest, ReservationResponse.class);

    ReservationResponse res = response.getBody();
    HttpStatusCode statusCode = response.getStatusCode();
    RestaurantTableEntity savedReservationObj = null;

    if (statusCode.is2xxSuccessful() && res != null) {
      // Build and save local DB entity
      RestaurantTableEntity reservation = new RestaurantTableEntity(request.getCustomerId(),
          request.getTime(), request.getDate(), request.getPartySize());

      savedReservationObj = reservationRepository.save(reservation);
      logger.info("Successfully Saved Reservation Local ID: {}, Customer ID: {}",
          savedReservationObj.getId(), reservation.getCustomerId());
    } else {
      logger.error("Failed to process table assignment. HTTP Status Code: {}", statusCode.value());
      throw new RuntimeException("Table Service table assignment failed.");
    }

    if (res.getId() == null) {
      throw new RuntimeException(
          "Table Service returned a successful response, but the Reservation ID is missing.");
    }

    Long reservid = savedReservationObj.getId();
    ReservationStatus status = savedReservationObj.getStatus();

    // Create the final response object utilizing the matching constructor fields
    ReservationResponse finalResponse = new ReservationResponse(reservid, status.name(),
        res.getExpiresAt(), request.getPartySize());

    // 🚀 Publish an Asynchronous Event to RabbitMQ (Fire-and-forget)
    try {
      // = "order.exchange", "order.created"
      logger.info("Publishing reservation event to RabbitMQ for Order Queue...");
      rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, // "order.exchange"
          RabbitConfig.ROUTING_KEY, // "order.created"
          finalResponse // Transports the populated object layout safely
      );
      logger.info("Successfully sent message event payload to RabbitMQ exchange: {}",
          RabbitConfig.EXCHANGE_NAME);
    } catch (Exception amqpEx) {
      // Keeps primary data transaction intact if messaging middleware loses connectivity
      logger.error("Database transaction succeeded but RabbitMQ transmission failed!", amqpEx);
    }

    return finalResponse;
  }


  public RestaurantTableEntity getReservation(Long id) {
    RestaurantTableEntity reservation = reservationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
    return reservation;

  }

  public List<RestaurantTableEntity> getAllReservation() {

    List<RestaurantTableEntity> reservation = reservationRepository.findAll();
    return reservation;

  }



  public ReservationResponse confirm(Long id) {

    RestaurantTableEntity reservation = reservationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    // Prevent confirming already-confirmed/canceled reservations
    if (reservation.getStatus() != ReservationStatus.PENDING) {
      throw new IllegalStateException(
          "Reservation cannot be confirmed because it is " + reservation.getStatus());
    }
    // Update status
    reservation.setStatus(ReservationStatus.CONFIRMED);
    RestaurantTableEntity savedReservation = reservationRepository.save(reservation);

    // Build response with _links
    ReservationResponse response = new ReservationResponse();
    // Convert the String or ID object's string representation to a Long
    String idStr = String.valueOf(savedReservation.getId());
    response.setId(Long.parseLong(idStr));
    response.setExpiresAt(savedReservation.getUpdatedAt());
    response.setStatus(savedReservation.getStatus().name());
    return response;
  }
}
