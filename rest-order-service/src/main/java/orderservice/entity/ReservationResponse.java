package orderservice.entity;

import java.time.LocalDateTime;


public class ReservationResponse {

  private String Id;
  private LocalDateTime expiresAt;
  private String status;


  public ReservationResponse() {
    super();
  }


  public ReservationResponse(String id, String reservationStatus, LocalDateTime expiresAt) {
    this.Id = id;
    this.status = reservationStatus;
    this.expiresAt = expiresAt;

  }



  public ReservationResponse(LocalDateTime expiresAt) {
    super();
    this.expiresAt = expiresAt;
  }



  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
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
        + ", expiresAt=" + String.valueOf(expiresAt) + "]";
  }



}
