package tableservice.adapter.in.web;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tableservice.domain.model.ReservationRequest;
import tableservice.domain.model.TableReservation;
import tableservice.domain.port.out.ReservationRepository;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    
    @Autowired
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TableReservation createReservation(@RequestBody ReservationRequest reservationRequest) {
    	
    	logger.info("Number 1 ------> "+reservationRequest);
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
