package tableservice.adapter.out;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tableservice.RestaurantTableRepositoryAdapter;
import tableservice.domain.ApiLink;
import tableservice.domain.Links;
import tableservice.domain.ReservationResponse;
import tableservice.domain.ReservationStatus;
import tableservice.domain.RestaurantTable;
import tableservice.domain.RestaurantTableEntity;
import tableservice.domain.TableAvailabilityRequest;




@RestController
@RequestMapping("/api/tables")
public class ReservationController {

   @Autowired
   private  RestaurantTableRepositoryAdapter repositoryAdapter ;
	
	
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
   
  

    @PostMapping("/availability")
    public ResponseEntity<EntityModel<ReservationResponse>> checkAvailability(@RequestBody TableAvailabilityRequest request) {
        logger.info("Receiving reservation from customer: {}", request);

        LocalDate date = request.getDate();
        LocalTime time = request.getTime();
        int partySize = request.getPartySize();
        String customerId = request.getCustomerId();

        if (date == null || time == null || customerId == null) {
            throw new IllegalArgumentException("Date, time, and customerId must not be null");
        }

        LocalDateTime expiresAt = LocalDateTime.of(date, time).plusMinutes(15);
//
//        List<RestaurantTable> allTables = repositoryAdapter.findAllAvailable();
//        logger.info("Available tables: {}", allTables);
        
        RestaurantTable tableEntity = new RestaurantTable(partySize,customerId);
        RestaurantTable tableReservation = repositoryAdapter.save(tableEntity);
        
        System.out.println("Inside conteoller  after "+tableReservation.getId());
  
        Long reservationId = tableReservation.getId(); 
        ReservationResponse response = new ReservationResponse();
        response.setExpiresAt(expiresAt);
        response.setId(reservationId); 
        response.setStatus(tableReservation.getStatus().name());
        
            EntityModel<ReservationResponse> model = EntityModel.of(response);
            model.add(Link.of("/api/reservations/" + reservationId + "/confirm")
                          .withRel("confirm")
                          .withType("POST"));  

        logger.info("Returning reservation response: {}", model);
        return ResponseEntity.ok(model);
    }

    

      @GetMapping
      public List<RestaurantTable> getAllReservations() {
    	  System.out.println("HHHHHHHHHHHHH");
        return repositoryAdapter.findAllAvailable();
        		//findByStatus("AVAILABLE");
   }
    
    
    @GetMapping("/all")
    public  List<RestaurantTableEntity> getAll() {
        return repositoryAdapter.findAll();
    }
   
}
