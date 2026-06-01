package tableservice.adapter.web;
// adapter.in

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tableservice.AvailabilityRepositoryPort;
import tableservice.TableDefinitionService;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;
import tableservice.api.TableAvailabilityRequest;
import tableservice.api.TableDefinitionDTO;
import tableservice.domain.ReservationStatus;

@RestController
@RequestMapping("/api/tables")
public class TableController {

  private static final Logger logger = LoggerFactory.getLogger(TableController.class);

  private final TableDefinitionService tableService;
  private final AvailabilityRepositoryPort availRepositoryPort;


  public TableController(TableDefinitionService tableService,
      AvailabilityRepositoryPort availRepositoryPort) {
    this.tableService = tableService;
    this.availRepositoryPort = availRepositoryPort;
  }


  @GetMapping("/all")
  public List<TableDefinitionDTO> getAll() {
    return tableService.findAll();
  }


  @GetMapping("/size")
  public List<TableDefinitionDTO> getBySize(@RequestParam("size") int size) {
    return tableService.findBySize(size);
  }


  @GetMapping("/available")
  public ResponseEntity<List<ReservationResponse>> getAllavailable() {
    logger.info("Controller --- > Get availability");
    List<ReservationResponse> available = tableService.findTableByStatus();
    return ResponseEntity.ok(available);
  }

  @PostMapping("/availability")
  public ResponseEntity<ReservationResponse> checkAvailability(
      @RequestBody TableAvailabilityRequest request) {

    logger.info("Receiving reservation from rest-order: " + request);

    String customerId = request.getCustomerId();
    LocalDate date = request.getDate();
    LocalTime time = request.getTime();
    LocalDateTime startDateTime = LocalDateTime.of(date, time);


    LocalDateTime endDateTime = startDateTime.plusHours(2);

    int partySize = request.getPartySize();

    if (date == null || time == null || customerId == null) {
      throw new IllegalArgumentException("Date, time, and customerId must not be null");
    }

    // if (repositoryAdapter.existsByCustomerIdAndDateAndTime(customerId, date, time)) {
    // throw new IllegalArgumentException(
    // "Duplicate reservation detected for this customer, date, and time.");
    // }

    // ────────────────────────── Match table to party size ────────────────────────────────//
    List<TableAvailability> availableTables = tableService.findBestFitByCapacity(partySize);

    logger.info("BestFitListTables -------------------------------------------------->>> "
        + availableTables);

    if (availableTables.isEmpty()) {
      throw new IllegalArgumentException("No available table found for a party of " + partySize);
    }

    // Pick the best fit (smallest table that fits)
    TableAvailability matchedTable = availableTables.get(0);

    logger.info("BesTables ------------------------------>>> " + matchedTable);

    matchedTable.setReservationTime(time.plusHours(2));
    matchedTable.setStatus(ReservationStatus.PENDING);
    matchedTable.setCustomerId(customerId);
    matchedTable.setReservationDate(date);

    TableAvailability savedObject = availRepositoryPort.save(matchedTable);


    // ── Create availability recor d — status lives here ───────────

    // TableAvailability availability =
    // new TableAvailability(matchedTable, startDateTime, endDateTime, customerId, "RESERVED");
    //
    // LocalDateTime expiresAt = LocalDateTime.of(date, time).plusMinutes(15);
    //
    // // 1️⃣ Create and save TableDefination
    // TableDefination tableEntity = new TableDefination(matchedTable.getTableId(), partySize);
    //
    // TableDefination savedTable = tableRepositoryAdapter.save(tableEntity);
    // logger.info("Saved TableDefination --------------- --> " + savedTable);
    //
    // // 2️⃣ Create TableAvailability and link to saved TableDefination
    // TableAvailability availability2 =
    // new TableAvailability(savedTable, startDateTime, endDateTime, customerId, "PENDING");
    //
    // // 3️⃣ Save availability (cascades from TableDefination automatically)
    // TableDefination finalSaved = tableRepositoryAdapter.save(savedTable);
    //
    // TableAvailability tableAvailSavedObj = availaibilityRepositoryAdapter.save(availability);
    // // System.out.println("Saved TableAvailability --> " + finalSaved.getAvailability());
    //
    // 4️⃣ Build response
    ReservationResponse response = new ReservationResponse();
    response.setId(savedObject.getId());
    response.setStatus(savedObject.getStatus());
    LocalDateTime expiresAt =
        LocalDateTime.of(savedObject.getReservationDate(), savedObject.getReservationTime());
    response.setExpiresAt(expiresAt);

    // EntityModel<ReservationResponse> resource = EntityModel.of(response);
    return ResponseEntity.ok(response);
  }


}
