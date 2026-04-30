package tableservice.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Table;
import jakarta.persistence.*;
@Entity
@Table(name = "table_defination")  // matches DB table name
public class TableDefination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_id")  // assumes DB column name is table_id
    private String tableId;
    private int capacity;
   
    public TableDefination(String tableId, int capacity) {
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
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	@Override
	public String toString() {
		return "TableDefination [id=" + id + ", tableId=" + tableId + ", capacity=" + capacity + "]";
	}
    
    
}