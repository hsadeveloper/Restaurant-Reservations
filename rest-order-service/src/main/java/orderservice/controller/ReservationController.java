package orderservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import orderservice.entity.ReservationRequestDTO;
import orderservice.entity.ReservationResponse;
import orderservice.entity.RestaurantTableEntity;
import orderservice.service.ReservationService;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

  private final ReservationService reservationService;
  private final StreamBridge streamBridge;

  public ReservationController(ReservationService reservationService, StreamBridge streamBridge) {

    this.reservationService = reservationService;
    this.streamBridge = streamBridge;
  }

  @GetMapping("/available-tables")
  public ResponseEntity<List<ReservationRequestDTO>> getAvailableTables() {

    List<ReservationRequestDTO> tables = reservationService.getAvailableTables();

    return ResponseEntity.ok(tables);
  }

  @PostMapping("/")
  public DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> createReservation(
      @RequestBody ReservationRequestDTO request) {

    return reservationService.createReservation(request);
  }

  @PostMapping("/confirm/{id}")
  public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable("id") Long id) {

    ReservationResponse response = reservationService.confirm(id);

    boolean sent = streamBridge.send("confirmReservation-out-0", response);

    if (!sent) {
      throw new IllegalStateException("Failed to publish confirmation request to broker");
    }

    return ResponseEntity.ok(response);
  }

  @GetMapping("/all")
  public ResponseEntity<CollectionModel<RestaurantTableEntity>> getAllReservation() {

    List<RestaurantTableEntity> response = reservationService.getAllReservation();

    CollectionModel<RestaurantTableEntity> model = CollectionModel.of(response,
        linkTo(methodOn(ReservationController.class).getAllReservation()).withSelfRel());

    return ResponseEntity.ok(model);
  }
}
