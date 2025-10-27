package orderservice.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private LocalTime reservationTime;
    
    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = true)
    private Integer partySize;

    @Column(nullable = true)
    private Long tableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	public Reservation() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Reservation(String customerId, LocalTime reservationTime, LocalDate reservationDate, Integer partySize,
			Long tableId, ReservationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.customerId = customerId;
		this.reservationTime = reservationTime;
		this.reservationDate = reservationDate;
		this.partySize = partySize;
		this.tableId = tableId;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getCustomerId() {
		return customerId;
	}

	public LocalTime getReservationTime() {
		return reservationTime;
	}

	public Integer getPartySize() {
		return partySize;
	}

	public Long getTableId() {
		return tableId;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	

	public void setReservationTime(LocalTime localTime) {
		this.reservationTime = localTime;
	}

	public void setPartySize(Integer partySize) {
		this.partySize = partySize;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDate getReservationDate() {
		return reservationDate;
	}

	

	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
		
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", customerId=" + customerId + ", reservationTime=" + reservationTime
				+ ", reservationDate=" + reservationDate + ", partySize=" + partySize + ", tableId=" + tableId
				+ ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

	
	
}