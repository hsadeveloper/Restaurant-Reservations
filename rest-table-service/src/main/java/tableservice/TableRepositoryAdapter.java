package tableservice;

import java.util.List;

import org.springframework.stereotype.Repository;

import tableservice.domain.TableDefination;

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


	public List<TableDefination> findBestFitByCapacity(int partySize) {
		// TODO Auto-generated method stub
		return null;
	}


	public TableDefination getByTableId(String tableId) {
		// TODO Auto-generated method stub
		return null;
	}


	

}
