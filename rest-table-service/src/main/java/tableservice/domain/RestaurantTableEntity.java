package tableservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long id;

    private  String customerId;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;;
    
    
    public RestaurantTableEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public RestaurantTableEntity(Long id, String customerId, int capacity, ReservationStatus status) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.capacity = capacity;
		this.status = status;
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


	@Override
	public String toString() {
		return "RestaurantTableEntity [id=" + id + ", customerId=" + customerId + ", capacity=" + capacity + ", status="
				+ status + "]";
	}
    	 

}
