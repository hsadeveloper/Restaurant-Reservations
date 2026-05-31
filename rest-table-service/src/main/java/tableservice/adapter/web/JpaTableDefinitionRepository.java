package tableservice.adapter.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tableservice.adapter.out.persistence.TableDefinition;


@Repository

interface JpaTableDefinitionRepository extends JpaRepository<TableDefinition, Long> {

}
