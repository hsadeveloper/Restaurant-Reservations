package orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import orderservice.config.ReservationReplyTracker;
import orderservice.entity.ReservationRequestDTO;
import orderservice.entity.ReservationResponse;
import orderservice.entity.ReservationStatus;
import orderservice.entity.RestaurantTableEntity;
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
  private StreamBridge streamBridge;

  @Autowired
  private ReservationReplyTracker replyTracker;

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
    LocalTime startTime = LocalTime.of(BOOKING_START_HOUR, 0);
    LocalTime endTime = LocalTime.of(BOOKING_END_HOUR, 0);

    if (reservationLocalTime.isBefore(startTime) || reservationLocalTime.isAfter(endTime)) {
      String errMsg = String.format(
          "Booking Rejected: Outside operational hours. [Requested: %s], [Allowed: %d:00 - %d:00]",
          reservationLocalTime, BOOKING_START_HOUR, BOOKING_END_HOUR);
      logger.info(errMsg);
      throw new RuntimeException(errMsg);
    }

    // 📌 Business Rule 5: No overlapping reservations by same customer within an hour
    validateBooking(request.getCustomerId(), request.getDate(), request.getTime());

    // Build and save local DB entity
    RestaurantTableEntity reservation = new RestaurantTableEntity(request.getCustomerId(),
        request.getTime(), request.getDate(), request.getPartySize());

    RestaurantTableEntity savedReservationObj = reservationRepository.save(reservation);
    logger.info("Successfully Saved Reservation Local ID: {}, Customer ID: {}",
        savedReservationObj.getId(), reservation.getCustomerId());

    // 2. Unique ID to correlate the async reply back to this specific HTTP request
    String correlationId = UUID.randomUUID().toString();
    logger.debug("Generated correlationId: {}", correlationId);

    // 3. Create the DeferredResult — 5 second timeout before giving up
    DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> deferredResult =
        new DeferredResult<>(5000L);

    // If no reply arrives within 5s, respond with 504 instead of hanging forever
    deferredResult.onTimeout(() -> {
      logger.warn("Reservation processing timed out for correlationId: {}", correlationId);
      deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
          .body("Reservation processing timed out."));
    });

    // Register this correlationId + deferredResult pair so the async
    // Consumer<Message<ReservationResponse>> bean can resolve it later
    replyTracker.register(correlationId, deferredResult);
    logger.debug("Registered deferredResult in replyTracker for correlationId: {}", correlationId);

    // 4. Build the outgoing message with the correlationId as a header,
    // so the table-service can copy it back onto its reply
    Message<ReservationRequestDTO> message =
        MessageBuilder.withPayload(request).setHeader("correlationId", correlationId).build();

    logger.info("Publishing reservation request to 'brief-request-out-0' with correlationId: {}",
        correlationId);
    streamBridge.send("reservation-request-out-0", message);

    // Return a PENDING response — the controller will send the async request
    // via StreamBridge and update the client once the reply arrives.
    return new ReservationResponse(savedReservationObj.getId(), "PENDING", null,
        request.getPartySize());
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
