package orderservice.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerIdAndStatusAndReservationTimeBetween(
        String customerId,
        ReservationStatus status,
        LocalDateTime start,
        LocalDateTime end
    );

    List<Reservation> findByTableIdAndStatusAndReservationTime(
        Long tableId,
        ReservationStatus status,
        LocalDateTime time
    );

    List<Reservation> findByStatusAndCreatedAtBefore(
        ReservationStatus status,
        LocalDateTime threshold
    );

    Optional<Reservation> findByIdAndCustomerId(Long id, String customerId);
}