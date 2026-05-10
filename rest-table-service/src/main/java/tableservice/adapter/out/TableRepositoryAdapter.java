package tableservice.adapter.out;

import java.util.List;

import org.springframework.stereotype.Repository;

import tableservice.domain.TableDefination;
import tableservice.domain.port.in.TableRepository;
import tableservice.domain.port.in.TableRepositoryPort;

@Repository
public class TableRepositoryAdapter implements TableRepositoryPort {

	private final  TableRepository tableRepository;
	
    public TableRepositoryAdapter(TableRepository tableRepository) {
		this.tableRepository = tableRepository;
	}


	@Override
	public List<TableDefination> findAll() {
		return tableRepository.findAll();
	}


	@Override
	public TableDefination save(TableDefination table) {
	
		return tableRepository.save(table);
	}


	public TableDefination getByTableId(String tableId) {
		return tableRepository.findByTableId(tableId);
		
	}
	

}
