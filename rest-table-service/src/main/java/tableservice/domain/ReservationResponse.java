package tableservice.domain;

import java.time.LocalDateTime;



public class ReservationResponse {

  private Long id;
  private String status;
  private LocalDateTime expiresAt;

  public ReservationResponse() {

  }


  public ReservationResponse(Long id, String status, LocalDateTime expiresAt,
      Boolean availability) {
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
    return "ReservationResponse [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt
        + "]";
  }



}
