package orderservice.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import orderservice.entity.Reservation;
import orderservice.entity.ReservationStatus;
import orderservice.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationAutoCancelScheduler {

    @Autowired
    private ReservationRepository reservationRepository;

    // Run every 5 minutes
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void cancelPendingReservations() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
        List<Reservation> pendingReservations = reservationRepository
                .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);

        for (Reservation reservation : pendingReservations) {
            reservation.setStatus(ReservationStatus.CANCELED);
            reservationRepository.save(reservation);
            System.out.println("Auto-canceled reservation id=" + reservation.toString());
        }
    }
}


//[
// {
//     "customerId": "c-125",
//     "reservationTime": "19:00:00",
//     "reservationDate": "2025-10-22",
//     "partySize": 2,
//     "tableId": null,
//     "status": "PENDING",
//     "createdAt": "2025-10-18T22:50:24.93142",
//     "updatedAt": "2025-10-18T22:50:24.931426"
// },
// {
//     "customerId": "c-125",
//     "reservationTime": "19:00:00",
//     "reservationDate": "2025-10-22",
//     "partySize": 2,
//     "tableId": null,
//     "status": "PENDING",
//     "createdAt": "2025-10-18T22:55:26.82734",
//     "updatedAt": "2025-10-18T22:55:26.827344"
// },
// {
//     "customerId": "c-125",
//     "reservationTime": "19:00:00",
//     "reservationDate": "2025-10-22",
//     "partySize": 2,
//     "tableId": null,
//     "status": "PENDING",
//     "createdAt": "2025-10-18T22:57:40.664862",
//     "updatedAt": "2025-10-18T22:57:40.664866"
// },
// {
//     "customerId": "c-125",
//     "reservationTime": "19:00:00",
//     "reservationDate": "2025-10-21",
//     "partySize": 2,
//     "tableId": null,
//     "status": "PENDING",
//     "createdAt": "2025-10-19T10:56:10.300586",
//     "updatedAt": "2025-10-19T10:56:10.300594"
// }
//]