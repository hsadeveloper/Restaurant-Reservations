package tableservice.domain.port.in;

import java.util.List;


import tableservice.domain.TableDefination;

public interface TableRepositoryPort {
	
	 List<TableDefination> findAll();
	 TableDefination save(TableDefination table);  
	 

}
