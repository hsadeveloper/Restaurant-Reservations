package orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
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

  private static final int MAX_PARTY_SIZE = 6;
  private static final int BOOKING_START_HOUR = 11;
  private static final int BOOKING_END_HOUR = 22;
  private static final long REPLY_TIMEOUT_MS = 5000L;

  private final ReservationRepository reservationRepository;
  private final StreamBridge streamBridge;
  private final ReservationReplyTracker replyTracker;

  public ReservationService(ReservationRepository reservationRepository, StreamBridge streamBridge,
      ReservationReplyTracker replyTracker) {
    this.reservationRepository = reservationRepository;
    this.streamBridge = streamBridge;
    this.replyTracker = replyTracker;
  }

  public void validateBooking(String customerId, LocalDate date, LocalTime time) {
    boolean hasDoubleBooking = reservationRepository
        .existsByCustomerIdAndReservationDateAndReservationTime(customerId, date, time);
    if (hasDoubleBooking) {
      throw new IllegalStateException("Customer already has a booking at this time.");
    }
  }

  public DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> createReservation(
      ReservationRequestDTO request) {
    logger.info("ReservationRequestDTO.......................................................{}",
        request.toString());
    ZoneId zone = ZoneId.of("America/Chicago");
    ZonedDateTime reservationTime =
        LocalDateTime.of(request.getDate(), request.getTime()).atZone(zone);
    ZonedDateTime now = ZonedDateTime.now(zone);
    logger.info("Reservation Time: {}", reservationTime);
    logger.info("Current Time + 30min: {}", now.plusMinutes(30));

    if (reservationTime.isBefore(now.plusMinutes(30))) {
      throw new RuntimeException("Reservations must be at least 30 minutes in the future.");
    }

    if (request.getPartySize() > MAX_PARTY_SIZE) {
      logger.info("Party size > 6 .. {}", request.getPartySize());
      throw new RuntimeException("Maximum allowed party size per table is 6.");
    }

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

    validateBooking(request.getCustomerId(), request.getDate(), request.getTime());

    RestaurantTableEntity reservation = new RestaurantTableEntity(request.getCustomerId(),
        request.getTime(), request.getDate(), request.getPartySize());
    reservation.setTableId(request.getTableId());

    RestaurantTableEntity savedReservationObj = reservationRepository.save(reservation);
    logger.info("Successfully Saved Reservation Local ID: {}", savedReservationObj.getId());

    // Populating reservationId on the outgoing DTO
    request.setReservationId(savedReservationObj.getId());

    String correlationId = UUID.randomUUID().toString();
    logger.debug("Generated correlationId: {}", correlationId);

    DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> deferredResult =
        new DeferredResult<>(REPLY_TIMEOUT_MS);
    deferredResult.onTimeout(() -> {
      logger.warn("Reservation processing timed out for correlationId: {}", correlationId);
      deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
          .body("Reservation processing timed out."));
    });

    replyTracker.register(correlationId, deferredResult);
    logger.debug("Registered deferredResult in replyTracker for correlationId: {}", correlationId);

    Message<ReservationRequestDTO> message =
        MessageBuilder.withPayload(request).setHeader("correlationId", correlationId).build();

    logger.info("Publishing reservation request to 'brief-request-out-0' with correlationId: {}",
        correlationId);
    streamBridge.send("brief-request-out-0", message);
    return deferredResult;
  }

  @Bean
  public Consumer<Message<ReservationResponse>> processBrief() {
    return message -> {
      ReservationResponse payload = message.getPayload();
      logger.info("Received message from rest-table-service: tableId={}, reservationId={}",
          payload.getTableId(), payload.getId());

      String correlationId = (String) message.getHeaders().get("correlationId");
      if (correlationId == null) {
        logger.warn("Received reply with no correlationId, dropping: {}", payload);
        return;
      }

      if (payload.getId() != null) {
        reservationRepository.findById(payload.getId()).ifPresentOrElse(reservation -> {
          reservation.setTableId(payload.getTableId());

          if (payload.getStatus() != null) {
            reservation.setStatus(ReservationStatus.valueOf(payload.getStatus()));
          }

          reservationRepository.save(reservation);
          logger.info("Database Row ID {} updated successfully with TableID: {}", payload.getId(),
              payload.getTableId());
        }, () -> logger.error(
            "Critical Error: Database row ID {} not found during event reply handling!",
            payload.getId()));
      } else {
        logger.error(
            "Critical Error: Incoming payload did not provide a valid database tracking target ID!");
      }

      replyTracker.complete(correlationId, payload);
    };
  }

  public List<RestaurantTableEntity> getAllReservation() {
    return reservationRepository.findAll();
  }

  public ReservationResponse confirm(Long id) {
    RestaurantTableEntity reservation = reservationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    if (reservation.getStatus() != ReservationStatus.PENDING) {
      throw new IllegalStateException(
          "Reservation cannot be confirmed because it is " + reservation.getStatus());
    }

    reservation.setStatus(ReservationStatus.CONFIRMED);
    RestaurantTableEntity savedReservation = reservationRepository.save(reservation);

    ReservationResponse response = new ReservationResponse();
    response.setId(savedReservation.getId());
    response.setExpiresAt(savedReservation.getUpdatedAt());
    response.setStatus(savedReservation.getStatus().name());
    return response;
  }
}
