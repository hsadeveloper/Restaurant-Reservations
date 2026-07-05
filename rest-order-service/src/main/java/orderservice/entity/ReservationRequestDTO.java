package orderservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ReservationRequestDTO {

  @JsonFormat(pattern = "YYYY-MM-dd")
  private LocalDate date;

  @JsonFormat(pattern = "HH:mm:ss")
  private LocalTime time;
  private int partySize;
  private String customerId;

  public ReservationRequestDTO() {
    super();
  }

  public ReservationRequestDTO(LocalDate date, LocalTime time, int partySize, String customerId) {
    super();
    this.date = date;
    this.time = time;
    this.partySize = partySize;
    this.customerId = customerId;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public int getPartySize() {
    return partySize;
  }

  public void setPartySize(int partySize) {
    this.partySize = partySize;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  @Override
  public String toString() {
    return "CreateReservationRequest [date=" + date + ", time=" + time + ", partySize=" + partySize
        + ", customerId=" + customerId + "]";
  }



}
