package tableservice.adapter.out.persistence;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tableservice.AvailabilityRepositoryPort;
import tableservice.adapter.web.JpaAvailabilityRepository;
import tableservice.api.ReservationResponse;
import tableservice.domain.ReservationStatus;



@Component
public class AvailaibilityRepositoryAdapter implements AvailabilityRepositoryPort {



  private static final Logger logger =
      LoggerFactory.getLogger(AvailaibilityRepositoryAdapter.class);



  private final JpaAvailabilityRepository jpaAvailabilityRepository;


  public AvailaibilityRepositoryAdapter(JpaAvailabilityRepository jpaAvailabilityRepository) {
    this.jpaAvailabilityRepository = jpaAvailabilityRepository;
  }

  public TableAvailability save(TableAvailability availability) {
    return jpaAvailabilityRepository.save(availability);
  }

  @Override
  public List<ReservationResponse> findByStatus() {

    logger.info("inside JpaAvailabilityRepository --->");
    List<TableAvailability> tables =
        jpaAvailabilityRepository.findByStatus(ReservationStatus.AVAILABLE);

    logger.info("inside JpaAvailabilityRepository ---> " + tables);

    return tables.stream().map(e -> new ReservationResponse(e.getId(), e.getStatus())).toList();

  }

  @Override
  public List<TableAvailability> findBestFitCapacity(int size) {

    return jpaAvailabilityRepository.findByStatusAndSize(size);

  }

}
