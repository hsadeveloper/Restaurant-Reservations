package tableservice.adapter.out.persistence;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import tableservice.domain.ReservationStatus;

@Entity
@Table(name = "table_availability")
public class TableAvailability {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String customerId;

  @Column(nullable = false)
  private int capacity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReservationStatus status;

  private LocalDate reservationDate;
  private LocalTime reservationTime;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt; // ← added, needed for findByStatusAndCreatedAtBefore()

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now(); // ← auto-set on insert
  }

  public TableAvailability() {}

  public TableAvailability(Long id, String customerId, int capacity, ReservationStatus status) {
    this.id = id;
    this.customerId = customerId;
    this.capacity = capacity;
    this.status = status;
  }


  public TableAvailability(String customerId, int capacity, ReservationStatus status,
      LocalDate reservationDate, LocalTime reservationTime) {
    this.customerId = customerId;
    this.capacity = capacity;
    this.status = status;
    this.reservationDate = reservationDate;
    this.reservationTime = reservationTime;
  }



  public Long getId() {
    return id;
  }

  public String getCustomerId() {
    return customerId;
  }

  public int getCapacity() {
    return capacity;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public LocalDate getReservationDate() {
    return reservationDate;
  }

  public LocalTime getReservationTime() {
    return reservationTime;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public void setReservationDate(LocalDate reservationDate) {
    this.reservationDate = reservationDate;
  }

  public void setReservationTime(LocalTime reservationTime) {
    this.reservationTime = reservationTime;
  }

  @Override
  public String toString() {
    return "TableAvailability [id=" + id + ", customerId=" + customerId + ", capacity=" + capacity
        + ", status=" + status + ", reservationDate=" + reservationDate + ", reservationTime="
        + reservationTime + ", createdAt=" + createdAt + "]";
  }
}
