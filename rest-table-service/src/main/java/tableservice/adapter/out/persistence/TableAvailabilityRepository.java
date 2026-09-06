package tableservice.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tableservice.api.ReservationResponse;

public interface TableAvailabilityRepository extends JpaRepository<TableAvailability, Long> {

  @Query("""
        SELECT t FROM TableAvailability t
        WHERE t.status = 'AVAILABLE'
      """)
  List<ReservationResponse> checkAvailability();

}
