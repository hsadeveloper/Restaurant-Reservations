package tableservice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tableservice.domain.RestaurantTable;
import tableservice.domain.TableDefination;

public interface TableRepository extends JpaRepository<TableDefination, Long>{

	List<TableDefination> findAll();
	TableDefination findByTableId(String tableId);
	

}
