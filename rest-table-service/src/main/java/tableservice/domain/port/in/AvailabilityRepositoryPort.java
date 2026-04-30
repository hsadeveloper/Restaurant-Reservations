package tableservice.domain.port.in;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tableservice.domain.TableAvailability;
import tableservice.domain.TableDefination;

public interface AvailabilityRepositoryPort {
	
	 List<TableAvailability> findAll();
	 TableAvailability save(TableAvailability table);  
	 

}
