package tableservice.adapter.in.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tableservice.domain.model.ReservationRequest;
import tableservice.domain.model.ReservationResponse;
import tableservice.domain.model.TableReservation;
import tableservice.domain.port.out.ReservationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TableReservation createReservation(@RequestBody ReservationRequest reservationRequest) {
        // Convert the ReservationRequest to TableReservation
        TableReservation reservation = new TableReservation();
        reservation.setStatus("PENDING");
        reservation.setExpiresAt(LocalDateTime.parse(reservationRequest.getDate() + "T" + reservationRequest.getTime()));
        // Assuming reservationRequest contains date and time strings and some other properties like partySize, customerId
        // Further logic to handle the reservation creation and save it to the database
        
        return reservationRepository.create(reservation);
    }
    
 // Endpoint to get all reservations
    @GetMapping
    public List<TableReservation> getAllReservations() {
        return reservationRepository.findAll();
    }
   
}
