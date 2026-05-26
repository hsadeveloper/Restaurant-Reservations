package tableservice.port.out;

import java.util.List;
import tableservice.adapter.out.persistence.TableDefinition;

public interface TableDefinitionRepository {

  List<TableDefinition> findAll();
}
