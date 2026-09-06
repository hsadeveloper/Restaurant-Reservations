
package orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
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

  /*
   * Redis stores:
   *
   * Key = String Value = List<ReservationRequestDTO>
   */
  private final RedisTemplate<String, List<ReservationRequestDTO>> redisTemplate;

  public ReservationService(ReservationRepository reservationRepository, StreamBridge streamBridge,
      ReservationReplyTracker replyTracker,
      RedisTemplate<String, List<ReservationRequestDTO>> redisTemplate) {

    this.reservationRepository = reservationRepository;
    this.streamBridge = streamBridge;
    this.replyTracker = replyTracker;
    this.redisTemplate = redisTemplate;
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
        request);

    ZoneId zone = ZoneId.of("America/Chicago");

    ZonedDateTime reservationTime =
        LocalDateTime.of(request.getDate(), request.getTime()).atZone(zone);

    ZonedDateTime now = ZonedDateTime.now(zone);

    logger.info("Reservation Time: {}", reservationTime);

    logger.info("Current Time + 30min: {}", now.plusMinutes(30));

    /*
     * Reservation must be at least 30 minutes in the future.
     */
    if (reservationTime.isBefore(now.plusMinutes(30))) {

      throw new RuntimeException("Reservations must be at least 30 minutes in the future.");
    }

    /*
     * Maximum party size is 6.
     */
    if (request.getPartySize() > MAX_PARTY_SIZE) {

      logger.info("Party size > 6 .. {}", request.getPartySize());

      throw new RuntimeException("Maximum allowed party size per table is 6.");
    }

    LocalTime reservationLocalTime = reservationTime.toLocalTime();

    LocalTime startTime = LocalTime.of(BOOKING_START_HOUR, 0);

    LocalTime endTime = LocalTime.of(BOOKING_END_HOUR, 0);

    /*
     * Check restaurant operating hours.
     */
    if (reservationLocalTime.isBefore(startTime) || reservationLocalTime.isAfter(endTime)) {

      String errMsg = String.format(
          "Booking Rejected: Outside operational hours. " + "[Requested: %s], "
              + "[Allowed: %d:00 - %d:00]",
          reservationLocalTime, BOOKING_START_HOUR, BOOKING_END_HOUR);

      logger.info(errMsg);

      throw new RuntimeException(errMsg);
    }

    /*
     * Prevent duplicate booking for the same customer, date and time.
     */
    validateBooking(request.getCustomerId(), request.getDate(), request.getTime());

    /*
     * Create reservation entity.
     */
    RestaurantTableEntity reservation = new RestaurantTableEntity(request.getCustomerId(),
        request.getTime(), request.getDate(), request.getPartySize());

    /*
     * Set selected table ID.
     */
    reservation.setTableId(request.getTableId());

    /*
     * Save reservation to PostgreSQL.
     */
    RestaurantTableEntity savedReservationObj = reservationRepository.save(reservation);

    logger.info("Successfully Saved Reservation Local ID: {}", savedReservationObj.getId());

    /*
     * Put database reservation ID into the outgoing DTO.
     */
    request.setReservationId(savedReservationObj.getId());

    /*
     * Generate correlation ID so the asynchronous response can be matched to this request.
     */
    String correlationId = UUID.randomUUID().toString();

    logger.debug("Generated correlationId: {}", correlationId);

    DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> deferredResult =
        new DeferredResult<>(REPLY_TIMEOUT_MS);

    deferredResult.onTimeout(() -> {

      logger.warn("Reservation processing timed out " + "for correlationId: {}", correlationId);

      deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null));
    });

    /*
     * Register request so processBrief() can complete it when RabbitMQ responds.
     */
    replyTracker.register(correlationId, deferredResult);

    logger.debug("Registered deferredResult in replyTracker " + "for correlationId: {}",
        correlationId);

    /*
     * Create RabbitMQ message.
     */
    Message<ReservationRequestDTO> message =
        MessageBuilder.withPayload(request).setHeader("correlationId", correlationId).build();

    logger.info(
        "Publishing reservation request to " + "'brief-request-out-0' " + "with correlationId: {}",
        correlationId);

    /*
     * Send reservation request to rest-table-service.
     */
    boolean sent = streamBridge.send("brief-request-out-0", message);


    if (!sent) {

      logger.error("Failed to publish reservation request " + "with correlationId: {}",
          correlationId);

      deferredResult
          .setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));

      return deferredResult;
    }

    return deferredResult;
  }

  /*
   * Receives the reservation response from rest-table-service.
   */
  @Bean
  public Consumer<Message<ReservationResponse>> processBrief() {

    return message -> {

      ReservationResponse payload = message.getPayload();

      logger.info("Received message from rest-table-service: " + "tableId={}, reservationId={}",
          payload.getTableId(), payload.getId());

      String correlationId = (String) message.getHeaders().get("correlationId");

      if (correlationId == null) {

        logger.warn("Received reply with no correlationId, " + "dropping: {}", payload);

        return;
      }

      /*
       * Update the reservation in PostgreSQL.
       */
      if (payload.getId() != null) {

        reservationRepository.findById(payload.getId()).ifPresentOrElse(reservation -> {

          reservation.setTableId(payload.getTableId());

          if (payload.getStatus() != null) {

            reservation.setStatus(ReservationStatus.valueOf(payload.getStatus()));
          }

          reservationRepository.save(reservation);

          logger.info("Database Row ID {} updated " + "successfully with TableID: {}",
              payload.getId(), payload.getTableId());
        }, () -> logger.error(
            "Critical Error: Database row ID {} " + "not found during event " + "reply handling!",
            payload.getId()));

      } else {

        logger.error("Critical Error: Incoming payload did not provide "
            + "a valid database tracking target ID!");
      }

      /*
       * Complete the waiting REST request.
       */
      replyTracker.complete(correlationId, payload);
    };
  }

  /*
   * Get all reservations from PostgreSQL.
   */
  public List<RestaurantTableEntity> getAllReservation() {

    return reservationRepository.findAll();
  }

  /*
   * Confirm a reservation.
   */
  public ReservationResponse confirm(Long id) {

    RestaurantTableEntity reservation = reservationRepository.findByTableId(id)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    /*
     * Only PENDING reservations can be confirmed.
     */
    if (reservation.getStatus() != ReservationStatus.PENDING) {

      throw new IllegalStateException(
          "Reservation cannot be confirmed because it is " + reservation.getStatus());
    }

    reservation.setStatus(ReservationStatus.CONFIRMED);

    RestaurantTableEntity savedReservation = reservationRepository.save(reservation);

    /*
     * Create response DTO.
     */
    ReservationResponse response = new ReservationResponse();

    response.setId(savedReservation.getId());

    response.setTableId(savedReservation.getTableId());

    response.setExpiresAt(savedReservation.getUpdatedAt());

    response.setStatus(savedReservation.getStatus().name());

    /*
     * Publish confirmation through RabbitMQ.
     */
    boolean sent = streamBridge.send("confirmReservation-out-0", response);

    if (!sent) {

      throw new IllegalStateException("Could not send reservation confirmation message");
    }

    return response;
  }

  /*
   * Receives reservation confirmation messages.
   */
  @Bean
  public Consumer<Message<ReservationResponse>> confirmReservation() {

    return message -> {

      ReservationResponse response = message.getPayload();

      logger.info("Received reservation confirmation " + "for ID: {}, status: {}", response.getId(),
          response.getStatus());

      if (response.getId() != null) {

        logger.info("Reservation confirmation received " + "for reservation ID: {}",
            response.getId());

      } else {

        logger.warn("Received reservation response with null ID");
      }
    };
  }

  /*
   * Receives available tables from rest-table-service through RabbitMQ and saves them to Redis.
   */
  @Bean
  public Consumer<List<ReservationResponse>> pollavailTables() {

    return tables -> {

      logger.info("Received {} available tables", tables.size());

      /*
       * Convert ReservationResponse to ReservationRequestDTO before saving to Redis.
       *
       * This prevents HATEOAS links from ReservationResponse from being stored.
       */
      List<ReservationRequestDTO> availableTables = tables.stream().map(table -> {

        ReservationRequestDTO dto = new ReservationRequestDTO();

        /*
         * ReservationResponse.id represents the table ID.
         */
        dto.setTableId(table.getId());

        /*
         * Convert expiresAt to date and time if available.
         */
        if (table.getExpiresAt() != null) {

          dto.setDate(table.getExpiresAt().toLocalDate());

          dto.setTime(table.getExpiresAt().toLocalTime());
        }

        /*
         * ReservationResponse.size represents table capacity.
         */
        dto.setPartySize(table.getSize());

        /*
         * These fields are not available when polling available tables.
         */
        dto.setReservationId(null);

        dto.setCustomerId(null);

        return dto;
      }).toList();

      /*
       * Save the DTO list to Redis.
       */
      redisTemplate.opsForValue().set("available-tables", availableTables);

      logger.info("Successfully saved {} available tables to Redis", availableTables.size());
    };
  }

  /*
   * Get available tables from Redis.
   */
  public List<ReservationRequestDTO> getAvailableTables() {

    List<ReservationRequestDTO> tables = redisTemplate.opsForValue().get("available-tables");

    if (tables == null) {

      logger.info("No available tables found in Redis");

      return Collections.emptyList();
    }

    logger.info("Retrieved {} available tables from Redis", tables.size());

    return tables;
  }
}


