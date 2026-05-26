package orderservice.config;

import orderservice.repository.ReservationRepository;

public class Hasan extends ReservationAutoCancelScheduler {

  Hasan(ReservationRepository reservationRepository) {
    super(reservationRepository);
    // TODO Auto-generated constructor stub
  }



  // public Hasan(ReservationRepository reservationRepository) {
  // super(reservationRepository);
  // // TODO Auto-generated constructor stub
  // }
  //
  // @Override
  // protected void cancelPendingReservations() {
  // // TODO Auto-generated method stub
  // super.cancelPendingReservations();
  // }



}
