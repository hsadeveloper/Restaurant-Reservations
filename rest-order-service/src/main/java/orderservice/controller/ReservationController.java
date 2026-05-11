package orderservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import orderservice.entity.CreateReservationRequest;
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
      @RequestBody CreateReservationRequest request) {

    ReservationResponse response = reservationService.createReservation(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/")
  public ResponseEntity<ReservationResponse> createReservation1(
      @RequestBody CreateReservationRequest request) {
    ReservationResponse response = reservationService.createReservation(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }


  @PostMapping("/{id}/confirm")
  public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable("id") Long id) {
    ReservationResponse response = reservationService.confirm(id);

    return ResponseEntity.ok(response);
  }


  @GetMapping("/{id}")
  public ResponseEntity<RestaurantTableEntity> getReservation(@PathVariable("id") Long id) {
    RestaurantTableEntity response = reservationService.getReservation(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/all")
  public ResponseEntity<List<RestaurantTableEntity>> getAllReservation() {
    List<RestaurantTableEntity> response = reservationService.getAllReservation();
    return ResponseEntity.ok(response);
  }
}
