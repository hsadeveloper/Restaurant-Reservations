package tableservice.adapter.out.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tableservice.AvailabilityRepositoryPort;
import tableservice.ReservationStatus;
import tableservice.adapter.web.JpaAvailabilityRepository;
import tableservice.api.ReservationResponse;

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
    List<TableAvailability> tables = jpaAvailabilityRepository.findAvailable();
    logger.info("inside JpaAvailabilityRepository ---> " + tables);
    return tables.stream().map(e -> new ReservationResponse(e.getId(), e.getStatus().name()))
        .toList();
  }

  @Override
  public List<ReservationResponse> checkAvailability() {
    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();
    LocalTime cutoff = now.plusHours(2).plusMinutes(15);

    logger.info("Checking availability for date={} between {}--{}", today, now, cutoff);

    // 1. Fetch entities from database
    List<TableAvailability> tables = jpaAvailabilityRepository.checkAvailability();
    List<ReservationResponse> responses = new ArrayList<>();

    logger.info("Found {} available tables", tables.size());

    // 2. Map database entities to your API DTOs
    for (TableAvailability entity : tables) {
      ReservationResponse dto = new ReservationResponse();

      logger.info("Found ::: Id ", entity.getId());

      logger.info("Found {} one ::: available recoreds ", entity);

      dto.setId(entity.getId());
      dto.setStatus(entity.getStatus().name());
      dto.setSize(entity.getCapacity());
      LocalDate reservationDate = entity.getReservationDate();
      LocalTime reservationTime = entity.getReservationTime();
      if (reservationDate != null && reservationTime != null) {
        LocalDateTime combinedDateTime = LocalDateTime.of(reservationDate, reservationTime);
        dto.setExpiresAt(combinedDateTime);
      } else {
        dto.setExpiresAt(null);
      }
      responses.add(dto);
    }
    return responses;
  }

  @Override
  public List<TableAvailability> findByStatusAndCreatedAtBefore(ReservationStatus status,
      LocalDateTime time) {

    return jpaAvailabilityRepository.findByStatusAndCreatedAtBefore(status, time);
  }

  @Override
  public TableAvailability confirm(Long id) {
    return jpaAvailabilityRepository.getById(id);
  }

  @Override
  public List<TableAvailability> findAvailable() {
    return jpaAvailabilityRepository.findAvailable();
  }
}
