package orderservice.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import orderservice.entity.ReservationStatus;
import orderservice.entity.RestaurantTableEntity;

public interface ReservationRepository extends JpaRepository<RestaurantTableEntity, Long> {

  List<RestaurantTableEntity> findAllByStatusAndCreatedAtBefore(ReservationStatus status,
      LocalDateTime time);

  boolean existsByCustomerIdAndReservationTimeBetween(String customerId, LocalDateTime start,
      LocalDateTime end);


  RestaurantTableEntity save(RestaurantTableEntity reservation);

  List<RestaurantTableEntity> findByStatusAndCreatedAtBefore(ReservationStatus status,
      LocalDateTime time);

  boolean existsByCustomerIdAndReservationDateAndReservationTime(String customerId, LocalDate date,
      LocalTime time);


}
