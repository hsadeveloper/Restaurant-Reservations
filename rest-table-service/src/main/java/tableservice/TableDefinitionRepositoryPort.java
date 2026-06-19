package tableservice;

import java.util.List;
import tableservice.api.TableDefinitionDTO;

public interface TableDefinitionRepositoryPort {

  List<TableDefinitionDTO> findAll();

  List<TableDefinitionDTO> findBySize(int size);

}
