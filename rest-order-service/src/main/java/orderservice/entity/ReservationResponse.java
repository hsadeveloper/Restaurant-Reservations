package orderservice.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationResponse {

  @JsonProperty("id")
  @JsonAlias({"Id", "ID"})
  private Long id;

  @JsonProperty("expiresAt")
  private LocalDateTime expiresAt;

  @JsonProperty("status")
  private String status;

  @JsonProperty("size")
  private int size;

  public ReservationResponse() {}

  public ReservationResponse(Long id, String status, LocalDateTime expiresAt, int size) {
    this.id = id;
    this.status = status;
    this.expiresAt = expiresAt;
    this.size = size;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "ReservationResponse [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt
        + ", size=" + size + "]";
  }
}
