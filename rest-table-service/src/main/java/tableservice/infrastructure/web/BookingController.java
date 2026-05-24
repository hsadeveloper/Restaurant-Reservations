package tableservice.infrastructure.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tableservice.applcation.port.out.TableDefinationRepository;
import tableservice.domain.TableDefination;


@RestController
@RequestMapping("/api/tables")
public class BookingController {

  private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

  private TableDefinationRepository tableDefinationRepository;

  public BookingController(TableDefinationRepository tableDefinationRepository) {
    super();
    this.tableDefinationRepository = tableDefinationRepository;
  }



  @GetMapping
  public List<TableDefination> getAllReservations() {
    return tableDefinationRepository.findAll();
  }

  @GetMapping("/table")
  public ResponseEntity<TableDefination> getTable(@RequestParam("id") String tableId) {
    TableDefination result = tableRepositoryAdapter.getByTableId(tableId);
    if (result == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result);
  }

  // @GetMapping("/table")
  // public ResponseEntity<TableDefination> getTable(@RequestParam("id") String tableId) {
  // TableDefination result = tableRepositoryAdapter.getByTableId(tableId);
  // if (result == null) {
  // return ResponseEntity.notFound().build();
  // }
  // return ResponseEntity.ok(result);
  // }
  //
  // @PostMapping("/savetable")
  // public ResponseEntity<TableDefination> saveTable(@RequestBody TableDefination table) {
  // logger.info("Obkect from Controllerrrrrr --->********" + table.toString());
  // TableDefination saved = tableRepositoryAdapter.save(table);
  // return ResponseEntity.ok(saved);
  // }
  //
  // @GetMapping("/all")
  // public List<TableDefination> getAllTables() {
  // return tableRepositoryAdapter.findAll();
  // }
  //
  // // get available table by this date and time
  //
  //
  //
  // // Match the URL: /api/tables/availabille/{size}
  // @GetMapping("/availabille/{size}")
  // public ResponseEntity<List<TableDefination>> getAvailableTables(@PathVariable("size") int size)
  // {
  //
  // List<TableDefination> bestFitTables = tableDefinationRepository.findBestFitByCapacity(size);
  //
  // if (bestFitTables.isEmpty()) {
  // return ResponseEntity.noContent().build();
  // }
  //
  // return ResponseEntity.ok(bestFitTables);
  // }
  //
  //
  //
  // @PostMapping("/availability")
  // public ResponseEntity<EntityModel<ReservationResponse>> checkAvailability(
  // @RequestBody TableAvailabilityRequest request) {
  //
  // logger.info("Receiving reservation from rest-order: " + request);
  //
  // String customerId = request.getCustomerId();
  // LocalDate date = request.getDate();
  // LocalTime time = request.getTime();
  // LocalDateTime startDateTime = LocalDateTime.of(date, time);
  //
  //
  // LocalDateTime endDateTime = startDateTime.plusHours(2);
  //
  // int partySize = request.getPartySize();
  //
  // if (date == null || time == null || customerId == null) {
  // throw new IllegalArgumentException("Date, time, and customerId must not be null");
  // }
  //
  // if (repositoryAdapter.existsByCustomerIdAndDateAndTime(customerId, date, time)) {
  // throw new IllegalArgumentException(
  // "Duplicate reservation detected for this customer, date, and time.");
  // }
  //
  // // ── Match table to party size ────────────────────────────────
  // List<TableDefination> availableTables =
  // tableDefinationRepository.findBestFitByCapacity(partySize);
  //
  // logger.info("availableTables ----------------------------------------------------------->"
  // + availableTables);
  //
  // if (availableTables.isEmpty()) {
  // throw new IllegalArgumentException("No available table found for a party of " + partySize);
  // }
  //
  // // Pick the best fit (smallest table that fits)
  // TableDefination matchedTable = availableTables.get(0);
  //
  // // ── Create availability recor d — status lives here ───────────
  // /***** issue ****/
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
  // TableAvailability availability2 = new TableAvailability(savedTable, startDateTime, endDateTime,
  // // endDate
  // // =
  // // same
  // // day
  // // for
  // // a
  // // single
  // // reservation
  // customerId, "PENDING" // mark as unavailable once reserved
  // );
  //
  // // 3️⃣ Save availability (cascades from TableDefination automatically)
  // TableDefination finalSaved = tableRepositoryAdapter.save(savedTable);
  //
  // TableAvailability tableAvailSavedObj = availaibilityRepositoryAdapter.save(availability);
  // // System.out.println("Saved TableAvailability --> " + finalSaved.getAvailability());
  //
  // // 4️⃣ Build response
  // ReservationResponse response = new ReservationResponse();
  // response.setId(tableAvailSavedObj.getId());
  // response.setStatus("RESERVED");
  // response.setExpiresAt(expiresAt);
  //
  // EntityModel<ReservationResponse> resource = EntityModel.of(response);
  // return ResponseEntity.ok(resource);
  // }


}
