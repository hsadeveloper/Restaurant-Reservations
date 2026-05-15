package tableservice.adapter.out;

import org.springframework.stereotype.Component;
import tableservice.domain.TableAvailability;
// 1. Change this import to point to the actual repository location
import tableservice.domain.port.in.JpaAvailabilityRepository;

@Component
public class AvailaibilityRepositoryAdapter {

  // 2. Change the field type to match the new package path
  private final JpaAvailabilityRepository jpaAvailabilityRepository;

  // 3. Update the constructor argument type
  public AvailaibilityRepositoryAdapter(JpaAvailabilityRepository jpaAvailabilityRepository) {
    this.jpaAvailabilityRepository = jpaAvailabilityRepository;
  }

  public TableAvailability save(TableAvailability availability) {
    return jpaAvailabilityRepository.save(availability);
  }
}
