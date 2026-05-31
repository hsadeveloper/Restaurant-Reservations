package tableservice.adapter.out.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import tableservice.TableDefinitionRepositoryPort;
import tableservice.api.TableDefinitionDTO;

@Component
class TableDefinitionAdapter implements TableDefinitionRepositoryPort {

  private final TableDefinitionJpaRepository jpaRepository;


  public TableDefinitionAdapter(TableDefinitionJpaRepository jpaRepository) {
    super();
    this.jpaRepository = jpaRepository;
  }


  @Override
  public List<TableDefinitionDTO> findAll() {
    return jpaRepository.findAll().stream().map(TableDefinition::toDto).toList();
  }


  @Override
  public List<TableDefinitionDTO> findBySize(int size) {
    return jpaRepository.findByCapacity(size).stream().map(TableDefinition::toDto).toList();
  }



}
