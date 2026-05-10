package tableservice.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import tableservice.domain.TableAvailability;

// This is the actual Spring Data JPA interface
public interface JpaAvailabilityRepository extends JpaRepository<TableAvailability, Long> {
}
