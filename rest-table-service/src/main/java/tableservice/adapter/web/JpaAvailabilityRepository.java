package tableservice.adapter.web;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.domain.ReservationStatus;

public interface JpaAvailabilityRepository extends JpaRepository<TableAvailability, Long> {

  List<TableAvailability> findByStatus(ReservationStatus status);

  @Query("SELECT t FROM TableAvailability t WHERE t.status = 'AVAILABLE' AND t.capacity >= :size")
  List<TableAvailability> findByStatusAndSize(@Param("size") int size);

}
