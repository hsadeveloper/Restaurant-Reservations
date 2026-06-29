package tableservice.api;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class ReservationResponse {

  // FIX: Instructs Jackson to look strictly for lowercase "id" from the JSON packet
  @JsonProperty("id")
  @JsonAlias({"Id", "ID"})
  private Long id;

  private String status; // Uses String to safely parse your enum values across network lines
  private LocalDateTime expiresAt;
  private int size;

  public ReservationResponse() {}



  public ReservationResponse(Long id, String status) {
    super();
    this.id = id;
    this.status = status;
  }



  public ReservationResponse(Long id, String status, LocalDateTime expiresAt, int size) {
    super();
    this.id = id;
    this.status = status;
    this.expiresAt = expiresAt;
    this.size = size;
  }



  // Explicitly add property mapping markers over your getter and setter accessors
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  @JsonProperty("id")
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
    return "ReservationResponse [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt
        + ", size=" + size + "]";
  }
}
