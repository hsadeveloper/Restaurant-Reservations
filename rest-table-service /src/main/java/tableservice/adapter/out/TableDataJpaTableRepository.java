package tableservice.adapter.out;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tableservice.domain.TableDefination;

@Repository
public interface TableDataJpaTableRepository extends JpaRepository<TableDefination, Long> {

  Optional<TableDefination> findByTableId(String tableId);

  Optional<TableDefination> findFirstByCapacity(int capacity);


}
