package tableservice.port.out;

import java.util.List;
import tableservice.api.TableDefinitionDTO;


public interface TableDefinitionRepository {

  List<TableDefinitionDTO> findAll();
}
