//package orderservice.config;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import orderservice.entity.Reservation;
//import orderservice.entity.ReservationStatus;
//import orderservice.repository.ReservationRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//public class ReservationAutoCancelScheduler {
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    // Run every 5 minutes
//    @Scheduled(fixedRate = 5 * 60 * 1000)
//    public void cancelPendingReservations() {
//        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
//        List<Reservation> pendingReservations = reservationRepository
//                .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);
//
//        for (Reservation reservation : pendingReservations) {
//            reservation.setStatus(ReservationStatus.CANCELED);
//            reservationRepository.save(reservation);
//            System.out.println("Auto-canceled reservation id=" + reservation.toString());
//        }
//    }
//}

