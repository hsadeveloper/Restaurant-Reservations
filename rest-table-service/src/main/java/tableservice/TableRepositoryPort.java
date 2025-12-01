package tableservice;

import java.util.List;

import tableservice.domain.TableAvailability;
import tableservice.domain.TableDefination;

public interface TableRepositoryPort {
	
	 List<TableDefination> findAll();
	 TableDefination save(TableDefination table);  // ✔ correct
	 TableDefination getByTableId(String tableId);

}
