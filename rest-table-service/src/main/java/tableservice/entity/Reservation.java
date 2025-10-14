package tableservice.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableId;
    private String customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public Reservation() {
    }

    public Reservation(Long tableId, String customerId, LocalDateTime startTime, LocalDateTime endTime) {
        this.tableId = tableId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Long getTableId() {
        return tableId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean overlaps(Reservation other) {
        return !startTime.isAfter(other.endTime) && !endTime.isBefore(other.startTime);
    }

    public Reservation addTurnoverBuffer(Integer bufferMinutes) {
        return new Reservation(tableId, customerId, startTime, endTime.plusMinutes(bufferMinutes));
    }
}
