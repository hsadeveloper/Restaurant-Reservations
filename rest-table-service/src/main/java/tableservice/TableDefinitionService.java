package tableservice;

import java.util.List;
import org.springframework.stereotype.Component;
import tableservice.api.TableDefinitionDTO;

@Component
public class TableDefinitionService {

  private final TableDefinitionRepositoryPort repositoryPort;


  public TableDefinitionService(TableDefinitionRepositoryPort repositoryPort) {
    this.repositoryPort = repositoryPort;
  }


  public List<TableDefinitionDTO> findAll() {
    List<TableDefinitionDTO> tables = repositoryPort.findAll();

    if (tables.isEmpty()) {
      throw new RuntimeException("No tables found");
    }

    return tables;
  }

  public List<TableDefinitionDTO> findBySize(int size) {

    List<TableDefinitionDTO> tables = repositoryPort.findBySize(size);

    if (tables.isEmpty()) {
      throw new RuntimeException("No tables found with size: " + size);
    }

    return tables;
  }


}
