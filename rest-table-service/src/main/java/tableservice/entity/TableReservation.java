package tableservice.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Table; // ✅ For Jakarta EE (Spring Boot 3+)

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "table_reservations")
public class TableReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private Long tableId;

    @Column(nullable = false)
    private LocalDateTime slotStart;

    @Column(nullable = false)
    private LocalDateTime slotEnd;

    @Column(nullable = false)
    private String status; // CONFIRMED, CANCELED

    @Column(nullable = false)
    private LocalDateTime createdAt;

	public TableReservation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableReservation(String customerId, Long tableId, LocalDateTime slotStart, LocalDateTime slotEnd,
			String status, LocalDateTime createdAt) {
		super();
		this.customerId = customerId;
		this.tableId = tableId;
		this.slotStart = slotStart;
		this.slotEnd = slotEnd;
		this.status = status;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public Long getTableId() {
		return tableId;
	}

	public LocalDateTime getSlotStart() {
		return slotStart;
	}

	public LocalDateTime getSlotEnd() {
		return slotEnd;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public void setSlotStart(LocalDateTime slotStart) {
		this.slotStart = slotStart;
	}

	public void setSlotEnd(LocalDateTime slotEnd) {
		this.slotEnd = slotEnd;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}