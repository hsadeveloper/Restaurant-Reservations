package tableservice.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "table_availability")
public class TableAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "table_id") // this should reference PK (id)
    private TableDefination table;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    
    private String custId;

    // ─── Constructors ───────────────────────────────────────────
    public TableAvailability() {}

    public TableAvailability(TableDefination table, LocalDateTime startDate, LocalDateTime endDate, String status) {
        this.table     = table;
        this.startDate = startDate;
        this.endDate   = endDate;
        this.status    = status;
    }
      

    public TableAvailability(TableDefination table, LocalDateTime startDate, LocalDateTime endDate, String custId ,String status) {
		super();
		this.table = table;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.custId = custId;
	}

	// ─── Getters ────────────────────────────────────────────────
    public Long getId() {
        return id;
    }

    public TableDefination getTable() {
        return table;
    }

    public String getTableId() {
        return table != null ? table.getTableId() : null;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    // ─── Setters ────────────────────────────────────────────────
    public void setTable(TableDefination table) {
        this.table = table;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ─── Utility ────────────────────────────────────────────────
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(this.status);
    }

    public boolean overlaps(LocalDateTime date) {
        return (date.isEqual(startDate) || date.isAfter(startDate))
            && (date.isEqual(endDate)   || date.isBefore(endDate));
    }
    
    

    public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	@Override
	public String toString() {
		return "TableAvailability [id=" + id + ", table=" + table + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", status=" + status + ", custId=" + custId + "]";
	}


}