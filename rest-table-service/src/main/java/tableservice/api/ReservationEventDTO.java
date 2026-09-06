package tableservice.api;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationEventDTO {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("status")
  private String status;

  @JsonProperty("expiresAt")
  private LocalDateTime expiresAt;

  @JsonProperty("size")
  private int size;

  public ReservationEventDTO() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "ReservationEvent [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt
        + ", size=" + size + "]";
  }
}
