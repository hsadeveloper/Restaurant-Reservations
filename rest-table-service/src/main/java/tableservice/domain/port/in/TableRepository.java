package tableservice.domain.port.in;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tableservice.domain.TableDefination;

public interface TableRepository extends JpaRepository<TableDefination, Long>{

	List<TableDefination> findAll();
	TableDefination findByTableId(String tableId);
	

}
