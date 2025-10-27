package orderservice.controlle;

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
import orderservice.entity.Reservation;
import orderservice.entity.ReservationResponse;
import orderservice.entity.ReservationService;


// ============ CONTROLLER ============
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
             @RequestBody CreateReservationRequest request) {
        System.out.println("receieving  reservation form customer from order  (1)   ********************************88 :"+request.toString());
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(
            @PathVariable Long id) {
        System.out.println("Fetching reservation: {}"+ id);
        Reservation response = reservationService.getReservation(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservation() {
        System.out.println("Fetching All reservation........................................");
        List<Reservation> response = reservationService.getAllReservation();
        return ResponseEntity.ok(response);
    }
}
