package tableservice.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tableservice.domain.TableAvailability;
import tableservice.domain.TableDefination;

public interface AvailaibilityRepository extends JpaRepository<TableAvailability, Long> {
	
	List<TableAvailability> findAll();
	TableAvailability findByTableId(String tableId);

}
