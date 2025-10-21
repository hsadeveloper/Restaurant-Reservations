package tableservice.adapter.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tableservice.application.port.in.TableRepositoryAdapter;
import tableservice.application.port.out.ReservationRepository;
import tableservice.domain.Link;
import tableservice.domain.Links;
import tableservice.domain.ReservationResponse;
import tableservice.domain.TableAvailabilityRequest;
import tableservice.domain.TableEntity;
import tableservice.domain.TableReservation;
import tableservice.domain.port.in.ReservationUseCase;

@RestController
@RequestMapping("/api/tables")
public class ReservationController {

   @Autowired
	private static  TableRepositoryAdapter tableRepositoryAdapter;
	
	
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
   
    
	@PostMapping("/availability")
    public ResponseEntity<ReservationResponse> checkAvailability( @RequestBody TableAvailabilityRequest request) {
    	 System.out.println("receieving  reservation form customer from order : "+request);
    	  // 1. Parse date and time from request
         LocalDate date = request.getDate();
         LocalTime time = request.getTime();
         int partySize = request.getPartySize();
         String customerId = request.getCustomerId();

         if (date == null || time == null || customerId == null) {
             throw new IllegalArgumentException("Date, time, and customerId must not be null");
         }

         LocalDateTime expiresAt = LocalDateTime.of(date, time).plusMinutes(15);

         // 2. Get all tables
         List<TableEntity> allTables = tableRepositoryAdapter.findAllAvailable();
         System.out.println("Printing all tables : "+allTables);

         // 3. Find the smallest available table using the domain service
//         RestaurantTable availableTable = reservationUseCase.findAvailableTable(allTables, partySize);
//         System.out.println("Printing availableTable: "+availableTable);

//        // Convert to LocalDateTime
//        LocalDateTime expiresAt = LocalDateTime.of(
//           date,
//            time
//        ).plusMinutes(15);
        Links links = new Links();
       // links.setSelf(new Link("/api/reservations/r-42", "GET"));
        links.setConfirm(new Link("/api/reservations/r-42/confirm", "POST"));
        //links.setCancel(new Link("/api/reservations/r-42/cancel", "DELETE"));
        //links.setUpdate(new Link("/api/reservations/r-42", "PUT"));
        // Build response
        
        
//        reservationRepository.get

        ReservationResponse response = new ReservationResponse();
        response.setExpiresAt(expiresAt);
        response.set_links(links);
        response.setStatus("PENDING");
        System.out.println("receieving  reservation form customer from order .................................: "+response.toString());
        return ResponseEntity.ok(response);
    }

    
// // Endpoint to get all reservations
//    @GetMapping
//  //  public List<TableReservation> getAllReservations() {
////        return reservationRepository.findAll();
//    }
    
    
//    @PostMapping("/")
//    public  List<TableReservation> createtableReservation() {
//        return reservationRepository.save();
//    }
//   
}
