package tableservice.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "table_availability")
public class TableAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", referencedColumnName = "table_id", unique = true)
    private TableDefination table;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    // ─── Constructors ───────────────────────────────────────────
    public TableAvailability() {}

    public TableAvailability(TableDefination table, LocalDate startDate, LocalDate endDate, String status) {
        this.table     = table;
        this.startDate = startDate;
        this.endDate   = endDate;
        this.status    = status;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    // ─── Setters ────────────────────────────────────────────────
    public void setTable(TableDefination table) {
        this.table = table;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ─── Utility ────────────────────────────────────────────────
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(this.status);
    }

    public boolean overlaps(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate))
            && (date.isEqual(endDate)   || date.isBefore(endDate));
    }

    @Override
    public String toString() {
        return "TableAvailability{" +
            "id=" + id +
            ", tableId=" + (table != null ? table.getTableId() : "null") +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", status='" + status + '\'' +
            '}';
    }
}