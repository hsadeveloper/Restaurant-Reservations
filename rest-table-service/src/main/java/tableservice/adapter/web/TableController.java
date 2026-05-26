package tableservice.adapter.web;
// adapter.in

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tableservice.api.TableDefinition;

@RestController
@RequestMapping("/api/tables")
public class TableController {


  // contrller shall call a service



  @GetMapping("/all")
  public List<TableDefinition> getAll() {
    return tableDefinitionAdapter.findAll();
  }

}
