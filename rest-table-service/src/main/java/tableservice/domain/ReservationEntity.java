package tableservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_entity")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private LocalDateTime expiresAt;
    private Integer tableNumber;

    
    
    public ReservationEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

    
	public ReservationEntity(Long id, String status, LocalDateTime expiresAt, Integer tableNumber) {
		super();
		this.id = id;
		this.status = status;
		this.expiresAt = expiresAt;
		this.tableNumber = tableNumber;
	}


	public ReservationEntity(Long id2, Long customerId, int capacity, String status2) {
		// TODO Auto-generated constructor stub
	}


	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }
}
