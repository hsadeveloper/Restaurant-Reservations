package orderservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import orderservice.entity.ReservationRequestDTO;
import orderservice.entity.ReservationResponse;
import orderservice.entity.RestaurantTableEntity;
import orderservice.service.ReservationService;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

  @Autowired
  private ReservationService reservationService;

  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody ReservationRequestDTO requestDTO) {

    ReservationResponse response = reservationService.createReservation(requestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/")
  public ResponseEntity<ReservationResponse> createReservation1(
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
