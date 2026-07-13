package tableservice.configuration;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tableservice.ReservationStatus;
import tableservice.adapter.out.persistence.AvailaibilityRepositoryAdapter;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;
import tools.jackson.databind.ObjectMapper;

@Component
class ReservationAutoCancelScheduler {

  private static final Logger logger =
      LoggerFactory.getLogger(ReservationAutoCancelScheduler.class);

  private final AvailaibilityRepositoryAdapter availaibilityRepositoryAdapter;
  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  // Spring 4.3+ automatically wires single-constructor components, @Autowired is optional here
  public ReservationAutoCancelScheduler(
      AvailaibilityRepositoryAdapter availaibilityRepositoryAdapter,
      StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
    this.availaibilityRepositoryAdapter = availaibilityRepositoryAdapter;
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  @Scheduled(fixedRate = 30 * 60 * 1000) // Runs every 3 minutes
  void pullAvailableTable1() {
    try {
      List<ReservationResponse> tables = availaibilityRepositoryAdapter.checkAvailability();

      // Fixed: Using the injected instance mapper instead of an invalid static call
      // RIGHT: Using your lowercase bean instance variable "objectMapper"
      String json = this.objectMapper.writeValueAsString(tables);

      redisTemplate.opsForValue().set("cached-available-tables", json);
      logger.info("Successfully cached {} available tables to Redis.", tables.size());
    } catch (Exception e) {
      logger.error("Failed to publish available tables to Redis", e);
    }
  }



  @Scheduled(fixedRate = 30 * 60 * 1000)
  void pullAvailableTable() {

    try {
      List<ReservationResponse> tables = availaibilityRepositoryAdapter.checkAvailability();

      String json = this.objectMapper.writeValueAsString(tables);
      redisTemplate.opsForValue().set("cached-available-tables", json);

    } catch (Exception e) {
      logger.error("Failed to publish to Redis", e);
    }
  }


  @Scheduled(fixedRate = 10 * 60 * 1000)
  void cancelPendingReservations() {
    LocalDateTime cutoff = LocalDateTime.now().minusHours(1);

    List<TableAvailability> pendingReservations = availaibilityRepositoryAdapter
        .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);

    logger.info(
        ">>> SCHEDULER TRIGGERED <<<\n" + "Process: Reservation Consistency Check\n" + "Time: {}\n",
        LocalDateTime.now(java.time.ZoneOffset.UTC));

    for (TableAvailability reservation : pendingReservations) {
      reservation.setStatus(ReservationStatus.CANCELED);
      availaibilityRepositoryAdapter.save(reservation);
      logger.info(">>> Auto-canceled reservation id <<<\n {}\n", reservation.toString());
    }
  }
}
