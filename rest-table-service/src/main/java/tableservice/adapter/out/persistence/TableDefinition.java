package tableservice.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tableservice.api.TableDefinitionDTO;

@Entity
@Table(name = "table_definition") // matches DB table name
public class TableDefinition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "table_id") // assumes DB column name is table_id
  private String tableId;

  private int capacity;

  public TableDefinition() {
    // Required by JPA
  }

  public TableDefinition(String tableId, int capacity) {
    this.tableId = tableId;
    this.capacity = capacity;
  }


  public Long getId() {
    return id;
  }

  public String getTableId() {
    return tableId;
  }

  public int getCapacity() {
    return capacity;
  }


  public TableDefinitionDTO toDto() {

    return new TableDefinitionDTO(this.tableId, this.capacity);
  }

}
