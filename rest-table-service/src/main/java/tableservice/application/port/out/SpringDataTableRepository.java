package tableservice.application.port.out;



import org.springframework.data.jpa.repository.JpaRepository;

import tableservice.domain.ReservationEntity;
import tableservice.domain.TableEntity;
import tableservice.domain.TableReservation;

import java.util.List;

public interface SpringDataTableRepository extends JpaRepository<TableEntity, Long> {

    List<TableReservation> findByStatus(String status); // ← this line is critical
}
