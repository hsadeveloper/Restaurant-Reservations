import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tableservice.TableDefinitionService;
import tableservice.api.TableDefinitionDTO;

@RestController
@RequestMapping("/api/tables")
public class TableController {

  private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

  private final TableDefinitionService tableService;

  public TableController(TableDefinitionService tableService) {
    this.tableService = tableService;
  }

  @GetMapping("/all")
  public List<TableDefinitionDTO> getAll() {
    return tableService.findAll();
  }

  @GetMapping("/size")
  public List<TableDefinitionDTO> getBySize(@RequestParam("size") int size) {
    return tableService.findBySize(size);
  }


}
