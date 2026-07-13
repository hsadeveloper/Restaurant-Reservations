package tableservice.adapter.web;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tableservice.ReservationStatus;
import tableservice.adapter.out.persistence.TableAvailability;

public interface JpaAvailabilityRepository extends JpaRepository<TableAvailability, Long> {

  List<TableAvailability> findByStatus(ReservationStatus status);

  @Query("SELECT t FROM TableAvailability t WHERE t.status = 'AVAILABLE' AND t.capacity >= :size ORDER BY t.capacity ASC")
  List<TableAvailability> findByStatusAndSize(@Param("size") int size);


  List<TableAvailability> findByStatusAndCreatedAtBefore(ReservationStatus status,
      LocalDateTime time);


  @Query("""
      SELECT t FROM TableAvailability t
      WHERE t.status = 'AVAILABLE'
      """)
  List<TableAvailability> checkAvailability();


}
