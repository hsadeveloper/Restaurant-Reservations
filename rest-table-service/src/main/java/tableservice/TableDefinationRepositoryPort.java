package tableservice;

import java.util.List;
import tableservice.api.TableDefinition;

public interface TableDefinationRepositoryPort {

  List<TableDefinition> findAll();

}
