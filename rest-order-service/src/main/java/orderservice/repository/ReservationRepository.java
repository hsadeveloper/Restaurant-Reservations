package orderservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import orderservice.entity.Reservation;
import orderservice.entity.ReservationStatus;
import orderservice.entity.RestaurantTableEntity;

public interface ReservationRepository extends JpaRepository<RestaurantTableEntity, Long> {
	
    List<Reservation> findAllByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime time);
    boolean existsByCustomerIdAndReservationTimeBetween(String customerId, 
            LocalDateTime start, 
            LocalDateTime end);
    
    RestaurantTableEntity save(RestaurantTableEntity reservation);
    List<Reservation> findByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime time);

    
}