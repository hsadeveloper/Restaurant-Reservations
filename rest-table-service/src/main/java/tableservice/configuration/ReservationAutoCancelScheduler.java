// package tableservice.configuration;
//
// import java.time.LocalDateTime;
// import java.util.List;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
// import tableservice.adapter.out.persistence.AvailaibilityRepositoryAdapter;
// import tableservice.adapter.out.persistence.TableAvailability;
// import tableservice.domain.ReservationStatus;
//
// @Component
// class ReservationAutoCancelScheduler {
//
// private static final Logger logger =
// LoggerFactory.getLogger(ReservationAutoCancelScheduler.class);
//
//
// private final AvailaibilityRepositoryAdapter availaibilityRepositoryAdapter;
//
// public ReservationAutoCancelScheduler(
// AvailaibilityRepositoryAdapter availaibilityRepositoryAdapter) {
// this.availaibilityRepositoryAdapter = availaibilityRepositoryAdapter;
// }
//
// @Scheduled(fixedRate = 10 * 60 * 1000)
// void cancelPendingReservations() {
// LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
//
//
// List<TableAvailability> pendingReservations = availaibilityRepositoryAdapter
// .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);
//
// logger.info(
// ">>> SCHEDULER TRIGGERED <<<\n" + "Process: Reservation Consistency Check\n" + "Time: {}\n",
// LocalDateTime.now(java.time.ZoneOffset.UTC));
//
// for (TableAvailability reservation : pendingReservations) {
// reservation.setStatus(ReservationStatus.CANCELED);
// availaibilityRepositoryAdapter.save(reservation);
// logger.info(">>> Auto-canceled reservation id <<<\n {}\n", reservation.toString());
// }
// }
// }
