package orderservice.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// ============ CONTROLLER ============
@RestController
@RequestMapping("/api/reservations")

public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<String> createReservation(
             @RequestBody CreateReservationRequest request) {
        //log.info("Creating reservation for customer: {}", request.getCustomerId());
        String response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @PostMapping("/{id}/confirm")
//    public ResponseEntity<ReservationResponse> confirmReservation(
//            @PathVariable Long id,
//            @RequestParam String customerId) {
//        log.info("Confirming reservation: {}", id);
//        ReservationResponse response = reservationService.confirmReservation(id, customerId);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ReservationResponse> getReservation(
//            @PathVariable Long id,
//            @RequestParam String customerId) {
//        log.info("Fetching reservation: {}", id);
//        ReservationResponse response = reservationService.getReservation(id, customerId);
//        return ResponseEntity.ok(response);
//    }
}
