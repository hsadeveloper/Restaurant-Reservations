package tableservice.api;


public class TableDefinitionDTO {

  private String tableId;
  private int capacity;

  public TableDefinitionDTO() {}

  public TableDefinitionDTO(String tableId, int capacity) {
    this.tableId = tableId;
    this.capacity = capacity;
  }

  public String getTableId() {
    return tableId;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
}
