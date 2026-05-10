package orderservice.entity;

import java.time.LocalDateTime;

public class ReservationResponse {
	private String Id;
	//private  String Status;	
    private Links _links;
    private LocalDateTime expiresAt;
    
    
    public ReservationResponse() {
		super();
	}

	public ReservationResponse(String id, LocalDateTime expiresAt, Links _links) {
		super();
		Id = id;
		//Status = status;
		this._links = _links;
		this.expiresAt = expiresAt;
	}


	public String getId() {
		return Id;
	}


	public Links get_links() {
		return _links;
	}

	public void setId(String id) {
		Id = id;
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
	    // String.valueOf safely handles null by returning the literal string "null"
	    return "ReservationResponse [Id=" + String.valueOf(Id) + 
	           ", _links=" + String.valueOf(_links) + 
	           ", expiresAt=" + String.valueOf(expiresAt) + "]";
	}

 
}
