package tableservice.api;

import java.time.LocalDateTime;
import tableservice.domain.ReservationStatus;



public class ReservationResponse {

  private Long id;
  private ReservationStatus status;
  private LocalDateTime expiresAt;

  public ReservationResponse() {}


  public ReservationResponse(Long id, ReservationStatus status, LocalDateTime expiresAt,
      Boolean availability) {
    super();
    this.id = id;
    this.status = status;
    this.expiresAt = expiresAt;


  }

  public ReservationResponse(Long id, ReservationStatus status) {
    this.id = id;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setStatus(ReservationStatus reservationStatus) {
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
