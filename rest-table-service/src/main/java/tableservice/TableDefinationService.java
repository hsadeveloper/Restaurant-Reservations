package tableservice;

import java.util.List;
import org.springframework.stereotype.Component;
import tableservice.api.TableDefinition;

@Component
public class TableDefinationService {

  private final TableDefinationRepositoryPort tableDefinationRepositoryPort;


  public TableDefinationService(TableDefinationRepositoryPort tableDefinationRepositoryPort) {
    super();
    this.tableDefinationRepositoryPort = tableDefinationRepositoryPort;
  }


  public List<TableDefinition> findAll() {
    List<TableDefinition> tables = tableDefinationRepositoryPort.findAll();

    if (tables.isEmpty()) {
      throw new RuntimeException("No tables found");
    }

    return tables;
  }


}
