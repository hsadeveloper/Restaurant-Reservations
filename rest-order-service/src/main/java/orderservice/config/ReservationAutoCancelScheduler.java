// package orderservice.config;
//
// import java.time.LocalDateTime;
// import java.util.List;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
// import orderservice.entity.ReservationStatus;
// import orderservice.entity.RestaurantTableEntity;
// import orderservice.repository.ReservationRepository;
//
//
// @Component
// class ReservationAutoCancelScheduler {
//
// private static final Logger logger =
// LoggerFactory.getLogger(ReservationAutoCancelScheduler.class);
//
//
// private ReservationRepository reservationRepository;
//
// public ReservationAutoCancelScheduler(ReservationRepository reservationRepository) {
// super();
// this.reservationRepository = reservationRepository;
// }
//
// @Scheduled(fixedRate = 5 * 60 * 1000)
// void cancelPendingReservations() {
// LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
//
// List<RestaurantTableEntity> pendingReservations =
// reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);
//
//
// logger.info(">>> SCHEDULER TRIGGERED <<<\n" + "Process: Reservation Consistency Check\n"
// + "Time: {}\n", LocalDateTime.now(java.time.ZoneOffset.UTC));
//
//
// for (RestaurantTableEntity reservation : pendingReservations) {
// reservation.setStatus(ReservationStatus.CANCELED);
// reservationRepository.save(reservation);
// logger.info(">>> Auto-canceled reservation id <<<\n {}\n", reservation.toString());
// }
//
// }
// }
//
