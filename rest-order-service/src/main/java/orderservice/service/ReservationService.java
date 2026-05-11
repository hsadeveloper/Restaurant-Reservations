package orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import orderservice.entity.CreateReservationRequest;
import orderservice.entity.Link;
import orderservice.entity.Links;
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


  public ReservationResponse createReservation(CreateReservationRequest request) {
    // ✅ Combine date and time to get a LocalDateTime
    ZoneId zone = ZoneId.of("America/Chicago");
    ZonedDateTime reservationTime =
        LocalDateTime.of(request.getDate(), request.getTime()).atZone(zone);
    ZonedDateTime now = ZonedDateTime.now(zone);

    logger.info("Reservation Time:     " + reservationTime);
    logger.info("Current Time + 30min: " + LocalDateTime.now(zone));

    if (reservationTime.isBefore(now.plusMinutes(30))) {
      throw new RuntimeException("Reservations must be at least 30 minutes in the future.");
    }
    logger.info("Request time:      " + reservationTime);
    logger.info("System time (+30): " + LocalDateTime.now().plusMinutes(30));

    logger.info("Inside createReservation   .. .............................................."
        + LocalDateTime.now());


    // ✅ 2. Max party size ≤ 6
    if (request.getPartySize() > MAX_PARTY_SIZE) {
      logger.info("Party size > 6   .. " + request.getPartySize());
      throw new RuntimeException("Maximum allowed party size per table is 6.");
    }

    if (reservationTime.isBefore(now.plusMinutes(30))) {
      throw new RuntimeException("Reservations must be booked at least 30 minutes in advance.");
    }


    // ✅ 4. Reservation within allowed hours (11:00–22:00)
    LocalTime reservationLocalTime = reservationTime.toLocalTime();

    LocalTime startTime = LocalTime.of(BOOKING_START_HOUR, 0); // 11:00
    LocalTime endTime = LocalTime.of(BOOKING_END_HOUR, 0); // 22:00

    if (reservationLocalTime.isBefore(startTime) || reservationLocalTime.isAfter(endTime)) {
      logger.info(
          "Booking Rejected: Outside operational hours. [Requested: {}], [Allowed: {}:00 - {}:00]",
          reservationLocalTime, BOOKING_START_HOUR, BOOKING_END_HOUR);
      throw new RuntimeException(
          "Booking Rejected: Outside operational hours. [Requested: {}], [Allowed: {}:00 - {}:00]");
    }

    validateBooking(request.getCustomerId(), request.getDate(), request.getTime());

    // Step 1: Combine date + time to LocalDateTime
    LocalDateTime reservationDateTime =
        LocalDateTime.parse(request.getDate() + "T" + request.getTime() // "2025-10-20T19:00"
        );

    RestaurantTableEntity reservation = new RestaurantTableEntity(request.getCustomerId(),
        request.getTime(), request.getDate(), request.getPartySize());

    // 2: Build Reservation entity
    TableAvailabilityRequest availabilityRequest = new TableAvailabilityRequest();
    availabilityRequest.setCustomerId(request.getCustomerId()); // assuming userId maps to
                                                                // customerId
    availabilityRequest.setDate(request.getDate().toString());
    availabilityRequest.setTime(request.getTime().toString());
    availabilityRequest.setPartySize(request.getPartySize());


    logger.info("Build Reservation entity :............................................ \n"
        + availabilityRequest);

    ResponseEntity<ReservationResponse> response =
        restTemplate.postForEntity("http://localhost:1987/api/tables/availability",
            availabilityRequest, ReservationResponse.class);

    // Extract the response
    ReservationResponse res = response.getBody();
    HttpStatusCode statusCode = response.getStatusCode();
    if (statusCode.is2xxSuccessful()) { // Better practice for checking 200 OK
      reservationRepository.save(reservation);
      if (statusCode.is2xxSuccessful()) {
        reservationRepository.save(reservation);

        logger.info("Successfully Saved Reservation\n" + "Status Code: {}\n" + "Customer ID: {}\n"
            + "Reservation ID: {}", statusCode.value(), reservation.getCustomerId());
      } else {
        logger.error("Failed to Save Reservation\n" + "Status Code: {}\n" + "Response Body: {}",
            statusCode.value(), response.getBody());
      }
    }

    if (res == null || res.getId() == null) {
      throw new RuntimeException(
          "Table Service returned a successful response, but the Reservation ID is missing.");
    }



    // Map the ID safely
    String reservationId = res.getId();
    return new ReservationResponse(reservationId, res.getExpiresAt(),
        new Links(new Link("/api/reservations/" + reservationId + "/confirm", "POST")));
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


    // ⭐ Prevent confirming already-confirmed/canceled reservations
    if (reservation.getStatus() != ReservationStatus.PENDING) {
      throw new IllegalStateException(
          "Reservation cannot be confirmed because it is " + reservation.getStatus());
    }

    // Update status
    reservation.setStatus(ReservationStatus.CONFIRMED);
    reservationRepository.save(reservation);

    // Build response with _links
    return new ReservationResponse(reservation.getCustomerId().toString(),
        reservation.getUpdatedAt(),
        new Links(new Link("/api/reservations/" + id + "/cancel", "POST")));
  }
}
