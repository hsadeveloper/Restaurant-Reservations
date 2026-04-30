
package tableservice.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "table_reservations")
public class TableReservation extends RepresentationModel<TableReservation> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;


    private String status;
   
    private String tableId;
    
    private LocalDate reservationDate;
    
    private LocalTime reservationTime;
    

    // Constructors
    public TableReservation() {
    }

   
    public TableReservation(String customerId, String status, LocalDate reservationDate, LocalTime reservationTime) {
        this.customerId = customerId;
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


	public String getStatus() {
		return status;
	}


	public String getTableId() {
		return tableId;
	}


	public LocalDate getReservationDate() {
		return reservationDate;
	}


	public LocalTime getReservationTime() {
		return reservationTime;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public void setTableId(String tableId) {
		this.tableId = tableId;
	}


	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}


	public void setReservationTime(LocalTime reservationTime) {
		this.reservationTime = reservationTime;
	}	
}
