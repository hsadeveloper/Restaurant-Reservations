package tableservice.adapter.out.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import tableservice.TableDefinationRepositoryPort;
import tableservice.api.TableDefinition;

@Component
class SpringDataTableDefinationAdapter implements TableDefinationRepositoryPort {

  private final TableDwefinationJpaRepository tableDwefinationJpaRepository;


  SpringDataTableDefinationAdapter(TableDwefinationJpaRepository tableDwefinationJpaRepository) {
    super();
    this.tableDwefinationJpaRepository = tableDwefinationJpaRepository;
  }



  @Override
  public List<TableDefinition> findAll() {

    return tableDwefinationJpaRepository.findAll().stream()
        .map(tableservice.adapter.out.persistence.TableDefinition::toDto).toList();
  }


}
