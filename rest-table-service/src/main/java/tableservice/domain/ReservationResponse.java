package tableservice.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;



public class ReservationResponse {

    private String id;  // Change to String, if you want "r-42" as the ID
    private String status;
    private LocalDateTime expiresAt;
    @JsonProperty("_links")
    private Links _links;
  
    
	public ReservationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ReservationResponse(String id, String status, LocalDateTime expiresAt, Links _links, Boolean availability) {
		super();
		this.id = id;
		this.status = status;
		this.expiresAt = expiresAt;
		this._links = _links;

	}


	public String getId() {
		return id;
	}
	public String getStatus() {
		return status;
	}
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	public Links get_links() {
		return _links;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}


	

	@Override
	public String toString() {
		return "ReservationResponse [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt + ", _links="
				+ _links +"]";
	}

    
    	
   
    
}
