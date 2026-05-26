package orderservice.config;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import orderservice.entity.Reservation;
import orderservice.entity.ReservationStatus;
import orderservice.repository.ReservationRepository;

@Component
class ReservationAutoCancelScheduler {


  private final ReservationRepository reservationRepository;


  ReservationAutoCancelScheduler(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }


  // protected packge scope
  // Run every 5 minutes
  @Scheduled(fixedRate = 5 * 60 * 1000)
  void cancelPendingReservations() {
    LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
    List<Reservation> pendingReservations =
        reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);


    // lambda creating at least one object

    // change
    // pendingReservations.forEach(new Consumer<Reservation>(){
    //
    // @Override
    // public void accept(Reservation reservation) {
    // reservation.setStatus(ReservationStatus.CANCELED);
    // reservationRepository.save(reservation);
    //
    // }});



    // change
    pendingReservations.forEach(reservation -> {
      reservation.setStatus(ReservationStatus.CANCELED);
      reservationRepository.save(reservation);

    });


    // no Object

    for (Reservation reservation : pendingReservations) {
      reservation.setStatus(ReservationStatus.CANCELED);
      reservationRepository.save(reservation);
      // logging
      // System.out.println("Auto-canceled reservation id=" + reservation.toString());
    }
  }
}


