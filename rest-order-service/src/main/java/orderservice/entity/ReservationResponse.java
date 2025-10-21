package orderservice.entity;

import java.time.LocalDateTime;

public class ReservationResponse {
	private String Id;
	private  String Status;	
    private Links _links;
    private LocalDateTime expiresAt;
    
    
    public ReservationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ReservationResponse(String id, String status, Links _links, LocalDateTime expiresAt) {
		super();
		Id = id;
		Status = status;
		this._links = _links;
		this.expiresAt = expiresAt;
	}


	public String getId() {
		return Id;
	}



	public String getStatus() {
		return Status;
	}



	public Links get_links() {
		return _links;
	}



	public void setId(String id) {
		Id = id;
	}



	public void setStatus(String status) {
		Status = status;
	}



	public void set_links(Links _links) {
		this._links = _links;
	}



	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}


	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}


	@Override
	public String toString() {
		return "TableAvailabilityResponse [Id=" + Id + ", Status=" + Status + ", _links=" + _links + "]";
	}
    
    

	


   
    
    
}
