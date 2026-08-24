package tableservice.adapter.web;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
// 1. ADD THIS CRITICAL IMPORT STATEMENT HERE:
import org.springframework.data.jpa.repository.Query;
import tableservice.ReservationStatus;
import tableservice.adapter.out.persistence.TableAvailability;

public interface JpaAvailabilityRepository extends JpaRepository<TableAvailability, Long> {

  List<TableAvailability> findByStatus(ReservationStatus status);

  // 2. This will now compile perfectly without errors!
  @Query("SELECT t FROM TableAvailability t WHERE t.status = 'AVAILABLE'")
  List<TableAvailability> findAvailable();

  List<TableAvailability> findByStatusAndCreatedAtBefore(ReservationStatus status,
      LocalDateTime time);

  @Query("""
          SELECT t FROM TableAvailability t
          WHERE t.status = 'AVAILABLE'
      """)
  List<TableAvailability> checkAvailability();
}
