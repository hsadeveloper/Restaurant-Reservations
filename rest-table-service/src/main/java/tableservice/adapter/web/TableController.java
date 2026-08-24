package tableservice.adapter.web;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tableservice.AvailabilityRepositoryPort;
import tableservice.TableDefinitionService;
import tableservice.api.ReservationResponse;
import tableservice.api.TableDefinitionDTO;

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
    logger.info("/api/tables --- > Get availability");
    List<ReservationResponse> available = tableService.findTableByStatus();
    return ResponseEntity.ok(available);
  }

  // @PostMapping("/availability")
  // public ReservationResponse checkAvailability(@RequestBody TableAvailabilityRequest request) {
  // logger.info("/api/tables/availability --- > Get availability and create a pending
  // reservation");
  // return tableService.checkAvailability(request);
  //
  // }



}
