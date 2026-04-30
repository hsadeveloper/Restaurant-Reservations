package tableservice.adapter.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tableservice.RestaurantTableRepositoryAdapter;
import tableservice.TableDefinationRepository;
import tableservice.TableRepositoryAdapter;
import tableservice.domain.ReservationResponse;
import tableservice.domain.TableAvailability;
import tableservice.domain.TableAvailabilityRequest;
import tableservice.domain.TableDefination;
import tableservice.domain.TableReservation;
import tableservice.domain.repository.AvailaibilityRepositoryAdapter;

@RestController
@RequestMapping("/api/tables")
public class ReservationController {
	

   private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

   @Autowired
   private  RestaurantTableRepositoryAdapter repositoryAdapter ;
   
   @Autowired
   private  TableRepositoryAdapter tableRepositoryAdapter;
   
   @Autowired
  private AvailaibilityRepositoryAdapter availaibilityRepositoryAdapter;
   
   @Autowired
   private TableDefinationRepository tableDefinationRepository;
	 
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
    	System.out.println("Obkect from Controllerrrrrr --->********"+table.toString());
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

        System.out.println("Receiving reservation from customer: " + request);
        logger.info("Receiving reservation from customer: {}", request);

        String customerId = request.getCustomerId();
        LocalDate date    = request.getDate();
        LocalTime time    = request.getTime();
        int partySize     = request.getPartySize();

        if (date == null || time == null || customerId == null) {
            throw new IllegalArgumentException("Date, time, and customerId must not be null");
        }

        if (repositoryAdapter.existsByCustomerIdAndDateAndTime(customerId, date, time)) {
            throw new IllegalArgumentException("Duplicate reservation detected for this customer, date, and time.");
        }
        
     // ── Match table to party size ────────────────────────────────
        List<TableDefination> availableTables = tableDefinationRepository.findBestFitByCapacity(partySize);
        if (availableTables.isEmpty()) {
            throw new IllegalArgumentException("No available table found for a party of " + partySize + ".");
        }
        
        // Pick the best fit (smallest table that fits)
        TableDefination matchedTable = availableTables.get(0);

        // ── Create availability record — status lives here ───────────
        TableAvailability availability = new TableAvailability(
            matchedTable,
            date,
            date,
            "RESERVED"   // ← status set here, not on TableDefination
        );
        

        LocalDateTime expiresAt = LocalDateTime.of(date, time).plusMinutes(15);

        // 1️⃣ Create and save TableDefination
        TableDefination tableEntity = new TableDefination("tableId123", partySize);
        TableDefination savedTable  = tableRepositoryAdapter.save(tableEntity);
        System.out.println("Saved TableDefination --> " + savedTable);

        // 2️⃣ Create TableAvailability and link to saved TableDefination
        TableAvailability availability = new TableAvailability(
            savedTable,
            date,
            date,           // endDate = same day for a single reservation
            "PENDING"   // mark as unavailable once reserved
        );
    
        // 3️⃣ Save availability (cascades from TableDefination automatically)
        TableDefination finalSaved = tableRepositoryAdapter.save(savedTable);
        
        TableAvailability tableAvailSavedObj = availaibilityRepositoryAdapter.save(availability);
        //System.out.println("Saved TableAvailability --> " + finalSaved.getAvailability());

        // 4️⃣ Build response
        ReservationResponse response = new ReservationResponse();
        response.setId(finalSaved.getId());
        response.setStatus("RESERVED");
        response.setExpiresAt(expiresAt);

        EntityModel<ReservationResponse> resource = EntityModel.of(response);
        return ResponseEntity.ok(resource);
    }


}
