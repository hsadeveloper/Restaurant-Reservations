package orderservice.entity;

import java.time.LocalDateTime;
import java.util.Map;


public class ReservationResponse {

  private String Id;
  private Map<String, Link> _links;
  private LocalDateTime expiresAt;
  private String status;


  public ReservationResponse() {
    super();
  }


  public ReservationResponse(String id, String reservationStatus, LocalDateTime expiresAt,
      Map<String, Link> _links) {
    this.Id = id;
    this.status = reservationStatus;
    this.expiresAt = expiresAt;
    this._links = _links;
  }



  public ReservationResponse(Map<String, Link> _links, LocalDateTime expiresAt) {
    super();
    this._links = _links;
    this.expiresAt = expiresAt;
  }



  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }



  public Map<String, Link> get_links() {
    return _links;
  }


  public void set_links(Map<String, Link> _links) {
    this._links = _links;
  }


  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }


  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public String getStatus() {
    return status;
  }



  public void setStatus(String status) {
    this.status = status;
  }


  @Override
  public String toString() {
    return "ReservationResponse [Id=" + String.valueOf(Id) + ", status=" + String.valueOf(status)
        + ", _links=" + String.valueOf(_links) + ", expiresAt=" + String.valueOf(expiresAt) + "]";
  }



}
