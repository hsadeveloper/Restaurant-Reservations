package tableservice.configuration;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private StreamBridge streamBridge;


  // Spring 4.3+ automatically wires single-constructor components, @Autowired is optional here
  public ReservationAutoCancelScheduler(
      AvailaibilityRepositoryAdapter availaibilityRepositoryAdapter, ObjectMapper objectMapper) {
    this.availaibilityRepositoryAdapter = availaibilityRepositoryAdapter;
    this.objectMapper = objectMapper;
  }


  // 1800000ms = 30 minutes (Your comment said 3 minutes, 3 mins would be 3 * 60 * 1000 = 180000)
  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void pollTableAvailability() {
    try {
      List<ReservationResponse> tables = availaibilityRepositoryAdapter.checkAvailability();

      logger.info(
          ">>> SCHEDULER TRIGGERED <<<" + "Process: Polling Available table \n" + "Time: {}\n");

      // TARGET THE EXACT EXCHANGE NAME IN STREAMBRIDGE DIRECTLY
      streamBridge.send("table-available-exchange", tables);

      logger.info("Successfully send  avilable tables through ..............", tables.size());
    } catch (Exception e) {
      logger.error("Failed to publishsed ..................", e);
    }
  }


  // @Scheduled(fixedRate = 30 * 60 * 1000)
  // void pullAvailableTable() {
  //
  // try {
  // List<ReservationResponse> tables = availaibilityRepositoryAdapter.checkAvailability();
  //
  // String json = this.objectMapper.writeValueAsString(tables);
  // redisTemplate.opsForValue().set("cached-available-tables", json);
  //
  // } catch (Exception e) {
  // logger.error("Failed to publish to Redis", e);
  // }
  // }


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
