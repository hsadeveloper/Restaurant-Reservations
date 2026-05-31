package tableservice.adapter.out.persistence;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

interface TableDefinitionJpaRepository extends JpaRepository<TableDefinition, Long> {

  Collection<TableDefinition> findByCapacity(int size);


}
