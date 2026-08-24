package tableservice.api;

import java.time.LocalDate;
import java.time.LocalTime;

public class TableAvailabilityRequest {

  private Long reservationId;
  private LocalDate date;
  private LocalTime time;
  private int partySize;
  private String customerId;
  private Long tableId;

  public TableAvailabilityRequest() {}

  public TableAvailabilityRequest(LocalDate date, LocalTime time, int partySize, String customerId,
      Long tableId) {
    this.date = date;
    this.time = time;
    this.partySize = partySize;
    this.customerId = customerId;
    this.tableId = tableId;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public void setReservationId(Long reservationId) {
    this.reservationId = reservationId;
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

  public Long getTableId() {
    return tableId;
  }

  public void setTableId(Long tableId) {
    this.tableId = tableId;
  }

  @Override
  public String toString() {
    return "TableAvailabilityRequest{" + "reservationId=" + reservationId + ", date=" + date
        + ", time=" + time + ", partySize=" + partySize + ", customerId='" + customerId + '\''
        + ", tableId=" + tableId + '}';
  }
}
