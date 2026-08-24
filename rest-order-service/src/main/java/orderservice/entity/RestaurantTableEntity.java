package orderservice.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurant_table_entity")
public class RestaurantTableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String customerId;
  private LocalTime reservationTime;
  private LocalDate reservationDate;
  private int partySize;
  private Long tableId;

  @Column(unique = true)
  private String correlationId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReservationStatus status = ReservationStatus.PENDING;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = false)
  private LocalDateTime updatedAt = LocalDateTime.now();

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public RestaurantTableEntity() {
    super();
  }

  public RestaurantTableEntity(String customerId, LocalTime reservationTime,
      LocalDate reservationDate, int partySize) {
    super();
    this.customerId = customerId;
    this.reservationTime = reservationTime;
    this.reservationDate = reservationDate;
    this.partySize = partySize;
  }

  public RestaurantTableEntity(Long id, String customerId, LocalTime reservationTime,
      LocalDate reservationDate, int partySize, ReservationStatus status, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super();
    this.id = id;
    this.customerId = customerId;
    this.reservationTime = reservationTime;
    this.reservationDate = reservationDate;
    this.partySize = partySize;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // NOTE: the old 5-arg constructor that took `tableId` directly has been
  // removed. A table can never be known at creation time -- it's only
  // assigned later by rest-table-service. Use the no-arg constructor +
  // setters instead, and set tableId later via setTableId(...) once the
  // async reply arrives.

  public Long getTableId() {
    return tableId;
  }

  public void setTableId(Long tableId) {
    this.tableId = tableId;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public LocalTime getReservationTime() {
    return reservationTime;
  }

  public void setReservationTime(LocalTime localTime) {
    this.reservationTime = localTime;
  }

  public Integer getPartySize() {
    return partySize;
  }

  public void setPartySize(int partySize) {
    this.partySize = partySize;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDate getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(LocalDate reservationDate) {
    this.reservationDate = reservationDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Reservation [id=" + id + ", correlationId=" + correlationId + ", customerId="
        + customerId + ", reservationTime=" + reservationTime + ", reservationDate="
        + reservationDate + ", partySize=" + partySize + ", tableId=" + tableId + ", status="
        + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
  }


}
