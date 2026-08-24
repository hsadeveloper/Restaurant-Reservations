package orderservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
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
  @Autowired
  private StreamBridge streamBridge;

  @Autowired
  private final ReservationReplyTracker replyTracker;

  // Ensure your constructor looks EXACTLY like this
  public ReservationController(ReservationService reservationService) {
    super();
    this.reservationService = reservationService;
    // CRITICAL FIX: Instantiate the mapper and register the time module
    this.mapper = new ObjectMapper();
    // this.mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    //
    // // Optional: Prevent crashes if the subscriber JSON has fields the controller doesn't use
    // this.mapper.configure(
    // com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.replyTracker = new ReservationReplyTracker();
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
    return reservationService.createReservation(request);
  }

  @PostMapping("/{id}/confirm")
  public ResponseEntity<EntityModel<ReservationResponse>> confirmReservation(
      @PathVariable("id") Long id) {

    ReservationResponse response = reservationService.confirm(id);
    LOGGER.info("Sending to table-confirmation-exchange ");
    streamBridge.send("table-confirmation-exchange", id);
    EntityModel<ReservationResponse> model = EntityModel.of(response,
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
