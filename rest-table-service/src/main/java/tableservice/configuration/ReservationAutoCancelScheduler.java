package tableservice.configuration;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
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
  private final ObjectMapper objectMapper;

  private StreamBridge streamBridge;

  public ReservationAutoCancelScheduler(
      AvailaibilityRepositoryAdapter availaibilityRepositoryAdapter, ObjectMapper objectMapper,
      StreamBridge streamBridge) {
    super();
    this.availaibilityRepositoryAdapter = availaibilityRepositoryAdapter;
    this.objectMapper = objectMapper;
    this.streamBridge = streamBridge;
  }

  // 1800000ms = 30 minutes (Your comment said 3 minutes, 3 mins would be 3 * 60 * 1000 = 180000)
  @Scheduled(fixedRate = 3 * 60 * 1000)
  public void pollTableAvailability() {

    try {

      List<ReservationResponse> tables = availaibilityRepositoryAdapter.checkAvailability();

      logger.info(">>> SCHEDULER TRIGGERED <<< Process: Polling Available Table | Time: {}",
          LocalDateTime.now());

      logger.info("Found {} available tables to publish", tables.size());

      boolean sent = streamBridge.send("pollavailTables-out-0", tables);

      logger.info("StreamBridge send result = {} | Number of tables = {}", sent, tables.size());

      if (!sent) {
        logger.error("FAILED to publish available tables");
      } else {
        logger.info("Successfully published available tables");
      }

    } catch (Exception e) {

      logger.error("Failed to publish available tables", e);
    }
  }

  @Scheduled(fixedRate = 15 * 60 * 1000)
  void cancelPendingReservations() {
    LocalDateTime cutoff = LocalDateTime.now().minusHours(1);

    List<TableAvailability> pendingReservations = availaibilityRepositoryAdapter
        .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);
    logger.info(
        ">>> SCHEDULER TRIGGERED <<<" + "Process: Reservation Status Check\n" + "Time: {}\n",
        LocalDateTime.now(java.time.ZoneOffset.UTC));
    for (TableAvailability reservation : pendingReservations) {
      reservation.setStatus(ReservationStatus.CANCELED);
      availaibilityRepositoryAdapter.save(reservation);
      logger.info(">>> Auto-canceled reservation id <<<\n {}\n", reservation.toString());
    }
  }
}
