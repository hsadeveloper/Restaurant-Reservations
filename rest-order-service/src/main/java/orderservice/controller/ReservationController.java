package orderservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import orderservice.config.RabbitConfig;
import orderservice.entity.ReservationRequestDTO;
import orderservice.entity.ReservationResponse;
import orderservice.entity.RestaurantTableEntity;
import orderservice.service.ReservationService;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

  private ReservationService reservationService;

  private RabbitTemplate rabbitTemplate;

  public ReservationController(ReservationService reservationService,
      RabbitTemplate rabbitTemplate) {
    super();
    this.reservationService = reservationService;
    this.rabbitTemplate = rabbitTemplate;
  }

  @GetMapping("/poll")
  public ResponseEntity<List<ReservationResponse>> pollMessage1() {
    LOGGER.info("Polling table array list batch from queue: {}", RabbitConfig.QUEUE_NAME);

    // Ensure the Type Reference explicitly imports the same class definition as your return list
    List<ReservationResponse> tablesBatch = rabbitTemplate.receiveAndConvert(
        RabbitConfig.QUEUE_NAME, new ParameterizedTypeReference<List<ReservationResponse>>() {});

    if (tablesBatch == null || tablesBatch.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(tablesBatch);
  }



  @PostMapping("/")
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody ReservationRequestDTO request) {

    ReservationResponse response = reservationService.createReservation(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }



  @PostMapping("/{id}/confirm")
  public ResponseEntity<EntityModel<ReservationResponse>> confirmReservation(
      @PathVariable("id") Long id) {

    ReservationResponse response = reservationService.confirm(id);

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
