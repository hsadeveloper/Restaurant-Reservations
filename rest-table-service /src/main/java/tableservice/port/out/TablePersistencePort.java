package tableservice.port.out;

import java.util.List;
import tableservice.domain.TableDefination;

public interface TablePersistencePort {


  List<TableDefination> findAll();

  TableDefination getByTableId(String tableId);

  TableDefination getBySize(int size);


}
