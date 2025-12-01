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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tableservice.RestaurantTableRepositoryAdapter;
import tableservice.TableRepositoryAdapter;
import tableservice.domain.ApiLink;
import tableservice.domain.Links;
import tableservice.domain.ReservationResponse;
import tableservice.domain.ReservationStatus;
import tableservice.domain.RestaurantTable;
import tableservice.domain.TableAvailability;
import tableservice.domain.TableAvailabilityRequest;
import tableservice.domain.TableDefination;
import tableservice.domain.TableReservation;

@RestController
@RequestMapping("/api/tables")
public class ReservationController {

   @Autowired
   private  RestaurantTableRepositoryAdapter repositoryAdapter ;
   @Autowired
   private  TableRepositoryAdapter tableRepositoryAdapter;
	
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
   
    @GetMapping
    public List<TableReservation> getAllReservations() {
      return repositoryAdapter.findAllAvailable();
    }
    
    @GetMapping("/table")
    public ResponseEntity<TableDefination> getTable(@RequestParam("id") String tableId) {
        TableDefination result = tableRepositoryAdapter.getByTableId(tableId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/savetable")
    public ResponseEntity<TableDefination> saveTable(@RequestBody TableDefination table) {
        TableDefination saved = tableRepositoryAdapter.save(table);
        return ResponseEntity.ok(saved);
    }

  
  
    @GetMapping("/all")
    public  List<TableDefination> getAllTables() {
        return tableRepositoryAdapter.findAll();
    }
    
    @PostMapping("/availability")
    public ResponseEntity<EntityModel<ReservationResponse>> checkAvailability(
            @RequestBody TableAvailabilityRequest request) {
    	
        System.out.println("Receiving reservation from custome  "+ request);
        
        logger.info("Receiving reservation from customer: {}", request);
        String customerId = request.getCustomerId();
        LocalDate date = request.getDate();
        LocalTime time = request.getTime();
        int partySize = request.getPartySize();
        

        if (date == null || time == null || customerId == null) {
            throw new IllegalArgumentException("Date, time, and customerId must not be null");
        }
        
        if (repositoryAdapter.existsByCustomerIdAndDateAndTime(customerId, date, time)) {
            throw new IllegalArgumentException("Duplicate reservation detected for this customer, date, and time.");
        }

        LocalDateTime expiresAt = LocalDateTime.of(date, time).plusMinutes(15);

        // ✅ Create TableReservation (correct entity)
        TableReservation tableEntity = new TableReservation(customerId,"PENDING", date, time);
        
        System.out.println("Saving Entity --> "+tableEntity);
        
        logger.info(" ------------  Receiving reservation from tableEntity: {}", tableEntity);
        
        // ✅ Save using adapter
        TableReservation saved = repositoryAdapter.save(tableEntity);

        System.out.println("saved   --> "+saved);
        
        // Build response
        ReservationResponse response = new ReservationResponse();
        response.setId(saved.getId());
        response.setStatus(saved.getStatus());
        response.setExpiresAt(expiresAt);

        EntityModel<ReservationResponse> resource = EntityModel.of(response);

        return ResponseEntity.ok(resource);
    }


}
