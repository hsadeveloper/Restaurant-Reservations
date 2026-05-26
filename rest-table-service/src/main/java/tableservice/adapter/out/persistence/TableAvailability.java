package tableservice.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "table_availability")
public class TableAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long id;

    private  String customerId;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;;
    
    
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    
    public TableAvailability() {
		super();
		// TODO Auto-generated constructor stub
	}


	public TableAvailability(Long id, String customerId, int capacity, ReservationStatus status) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.capacity = capacity;
		this.status = status;
	}
	
	


	public TableAvailability(String customerId, int capacity, ReservationStatus status, LocalDate reservationDate,
			LocalTime reservationTime) {
		super();
		this.customerId = customerId;
		this.capacity = capacity;
		this.status = status;
		this.reservationDate = reservationDate;
		this.reservationTime = reservationTime;
	}


	public Long getId() {
		return id;
	}


	public String getCustomerId() {
		return customerId;
	}


	public int getCapacity() {
		return capacity;
	}


	public ReservationStatus getStatus() {
		return status;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}


	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	


	public LocalDate getReservationDate() {
		return reservationDate;
	}


	public LocalTime getReservationTime() {
		return reservationTime;
	}


	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}


	public void setReservationTime(LocalTime reservationTime) {
		this.reservationTime = reservationTime;
	}


	@Override
	public String toString() {
		return "RestaurantTableEntity [id=" + id + ", customerId=" + customerId + ", capacity=" + capacity + ", status="
				+ status + "]";
	}
    	 

}
