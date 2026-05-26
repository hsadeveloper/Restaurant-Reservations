package orderservice.config;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import orderservice.entity.ReservationStatus;
import orderservice.entity.RestaurantTableEntity;
import orderservice.repository.ReservationRepository;

@Component
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5m")
class ReservationAutoCancelScheduler {

  private static final Logger logger =
      LoggerFactory.getLogger(ReservationAutoCancelScheduler.class);

  @Autowired
  private ReservationRepository reservationRepository;

  @Scheduled(fixedRate = 60000) // Run every minute for testing
  @SchedulerLock(name = "cancel_pending_reservation", lockAtMostFor = "5m")
  void cancelPendingReservations() {

    logger.info(">>> SCHEDULER TRIGGERED <<<\n" + "Process: Reservation Consistency Check\n"
        + "Time:    {}\n", LocalDateTime.now(java.time.ZoneOffset.UTC));

    LocalDateTime cutoff = LocalDateTime.now().minusHours(5);

    List<RestaurantTableEntity> pendingReservations =
        reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);


    pendingReservations.forEach(reservation -> {
      reservation.setStatus(ReservationStatus.CANCELED);
      reservationRepository.save(reservation);
      logger.info("Auto-canceled reservation id=" + reservation.toString());

    });

  }
}

