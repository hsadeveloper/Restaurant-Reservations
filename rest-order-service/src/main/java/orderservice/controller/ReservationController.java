 package orderservice.controller;

import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/tables")
public class ReservationController {

  private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

  // contrller shall call a service

  private final TableDefinitionService tableService;

  public ReservationController(TableDefinitionService tableService) {
    this.tableService = tableService;
  }

  @GetMapping("/all")
  public List<TableDefinition> getAll() {
    return tableDefinitionAdapter.findAll();
  public List<TableDefinitionDTO> getAll() {
    return tableService.findAll();
  }

  @GetMapping("/size")
  public List<TableDefinitionDTO> getBySize(@RequestParam("size") int size) {
    return tableService.findBySize(size);
  }


}