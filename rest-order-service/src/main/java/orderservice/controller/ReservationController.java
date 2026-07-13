package orderservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import orderservice.config.ReservationReplyTracker;
import orderservice.entity.ReservationRequestDTO;
import orderservice.entity.ReservationResponse;
import orderservice.entity.RestaurantTableEntity;
import orderservice.service.ReservationService;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

  private ReservationService reservationService;
  private final ObjectMapper mapper;
  private StringRedisTemplate stringRedisTemplate;

  @Autowired
  private StreamBridge streamBridge;

  @Autowired
  private ReservationReplyTracker replyTracker;

  // Ensure your constructor looks EXACTLY like this
  public ReservationController(ReservationService reservationService,
      StringRedisTemplate stringRedisTemplate) {
    super();
    this.reservationService = reservationService;
    this.stringRedisTemplate = stringRedisTemplate;

    // CRITICAL FIX: Instantiate the mapper and register the time module
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    // Optional: Prevent crashes if the subscriber JSON has fields the controller doesn't use
    this.mapper.configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }


  @GetMapping("/latest")
  public ResponseEntity<ReservationRequestDTO[]> getLatestAvailableTables() {
    try {
      // 1. Fetch the synced JSON block created by your TableSubscriber
      String jsonPayload = stringRedisTemplate.opsForValue().get("availableTables::latest");

      if (jsonPayload == null) {
        // If the 15-minute TTL cache window expired, return an empty array
        return ResponseEntity.ok(new ReservationRequestDTO[0]);
      }

      // 2. Parse the string value back into a readable DTO array
      ReservationRequestDTO[] tables = mapper.readValue(jsonPayload, ReservationRequestDTO[].class);

      // 3. Return the array with an HTTP 200 status code
      return ResponseEntity.ok(tables);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }



  /**
   * Creates a new reservation.
   *
   * Flow: 1. Validate + save the reservation locally (synchronous). 2. Generate a correlationId to
   * track the async reply. 3. Register a DeferredResult so the HTTP thread can be released while we
   * wait for the table-service's async confirmation. 4. Publish the request to RabbitMQ via
   * StreamBridge (fire-and-forget). 5. Return the DeferredResult — Spring holds the connection open
   * until either the reply arrives (via ReservationReplyConsumerConfig) or the 5s timeout fires.
   */
  @PostMapping("/")
  public DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> createReservation(
      @RequestBody ReservationRequestDTO request) {

    LOGGER.info("Received reservation creation request for customerId: {}",
        request.getCustomerId());

    // 1. Local validation + save (throws on business rule violations)
    ReservationResponse response = reservationService.createReservation(request);
    LOGGER.info("Local reservation saved with PENDING status. Local ID: {}", response.getId());

    // 2. Unique ID to correlate the async reply back to this specific HTTP request
    String correlationId = UUID.randomUUID().toString();
    LOGGER.debug("Generated correlationId: {}", correlationId);

    // 3. Create the DeferredResult — 5 second timeout before giving up
    DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> deferredResult =
        new DeferredResult<>(5000L);

    // If no reply arrives within 5s, respond with 504 instead of hanging forever
    deferredResult.onTimeout(() -> {
      LOGGER.warn("Reservation processing timed out for correlationId: {}", correlationId);
      deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
          .body("Reservation processing timed out."));
    });

    // Register this correlationId + deferredResult pair so the async
    // Consumer<Message<ReservationResponse>> bean can resolve it later
    replyTracker.register(correlationId, deferredResult);
    LOGGER.debug("Registered deferredResult in replyTracker for correlationId: {}", correlationId);

    // 4. Build the outgoing message with the correlationId as a header,
    // so the table-service can copy it back onto its reply
    Message<ReservationRequestDTO> message =
        MessageBuilder.withPayload(request).setHeader("correlationId", correlationId).build();

    LOGGER.info("Publishing reservation request to 'brief-request-out-0' with correlationId: {}",
        correlationId);
    streamBridge.send("brief-request-out-0", message);

    // 5. Return immediately — HTTP thread is released, response is sent
    // later once the async reply resolves this deferredResult
    return deferredResult;
  }


  @PostMapping("/{id}/confirm")
  public ResponseEntity<EntityModel<ReservationResponse>> confirmReservation(
      @PathVariable("id") Long id) {

    ReservationResponse response = reservationService.confirm(id);
    streamBridge.send("brief-request-out-0", "1");
    EntityModel<ReservationResponse> model = EntityModel.of(response,
        linkTo(methodOn(ReservationController.class).confirmReservation(id)).withRel("confirm"));
    return ResponseEntity.ok(model);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<RestaurantTableEntity>> getReservation(
      @PathVariable("id") Long id) {
    RestaurantTableEntity response = reservationService.getReservation(id);
    EntityModel<RestaurantTableEntity> model = EntityModel.of(response,
        linkTo(methodOn(ReservationController.class).confirmReservation(id)).withRel("confirm"));
    return ResponseEntity.ok(model);
  }

  @GetMapping("/all")
  public ResponseEntity<CollectionModel<RestaurantTableEntity>> getAllReservation() {
    List<RestaurantTableEntity> response = reservationService.getAllReservation();
    CollectionModel<RestaurantTableEntity> model = CollectionModel.of(response,
        linkTo(methodOn(ReservationController.class).getAllReservation()).withSelfRel());
    return ResponseEntity.ok(model);
  }
}
