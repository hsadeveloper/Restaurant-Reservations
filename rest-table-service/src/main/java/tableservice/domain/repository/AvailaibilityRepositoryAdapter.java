package tableservice.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import tableservice.domain.TableAvailability;
import tableservice.domain.port.in.AvailabilityRepositoryPort;

@Repository
public class AvailaibilityRepositoryAdapter implements AvailabilityRepositoryPort {



   private  AvailaibilityRepository  repository;
	
    public AvailaibilityRepositoryAdapter(AvailaibilityRepository repository) {
		this.repository = repository;
	}


	@Override
	public List<TableAvailability> findAll() {
		return repository.findAll();
	}


	@Override
	public TableAvailability save(TableAvailability table) {
		return repository.save(table);
	}

	
	
}
