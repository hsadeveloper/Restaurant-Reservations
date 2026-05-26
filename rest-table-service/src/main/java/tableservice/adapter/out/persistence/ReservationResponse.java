package tableservice.adapter.out.persistence;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;



public class ReservationResponse {

    private Long id;  // Change to String, if you want "r-42" as the ID
    private String status;
    private LocalDateTime expiresAt;
    
	public ReservationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ReservationResponse(Long id, String status, LocalDateTime expiresAt, Boolean availability) {
		super();
		this.id = id;
		this.status = status;
		this.expiresAt = expiresAt;


	}


	public Long getId() {
		return id;
	}
	public String getStatus() {
		return status;
	}
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setStatus(String reservationStatus) {
		this.status = reservationStatus;
	}
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	

	@Override
	public String toString() {
		return "ReservationResponse [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt + "]";
	}

    
    	
   
    
}
