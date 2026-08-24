package tableservice.api;

import java.time.LocalDateTime;
import org.springframework.hateoas.RepresentationModel;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ReservationResponse extends RepresentationModel<ReservationResponse> {

  private Long id;
  private Long tableId;
  private String status; // Uses String to safely parse your enum values across network lines
  private LocalDateTime expiresAt;
  private int size;

  public ReservationResponse() {}

  public ReservationResponse(Long id, String status) {
    super();
    this.id = id;
    this.status = status;
  }

  public ReservationResponse(Long tableId, String status, LocalDateTime expiresAt, int size) {
    super();
    this.tableId = tableId;
    this.status = status;
    this.expiresAt = expiresAt;
    this.size = size;
  }

  public Long getTableId() {
    return tableId;
  }

  public void setTableId(Long tableId) {
    this.tableId = tableId;
  }

  // Explicitly add property mapping markers over your getter and setter accessors
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }


  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getSize() {
    return size;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "ReservationResponse [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt
        + ", size=" + size + "]";
  }



}
