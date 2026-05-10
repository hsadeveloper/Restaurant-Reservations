package tableservice.adapter.out;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Repository;

import tableservice.domain.TableDefination;
import tableservice.domain.TableReservation;
import tableservice.domain.port.in.RestaurantTableRepositoryPort;
import tableservice.domain.port.in.TableReservationRepository;

@Repository
public class RestaurantTableRepositoryAdapter implements RestaurantTableRepositoryPort {

    private  TableReservationRepository reservationRepository;
    

    public RestaurantTableRepositoryAdapter(
        TableReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public TableReservation save(TableReservation reservation) {
        System.out.println("Saving reservation..." + reservation);
        TableReservation saved = reservationRepository.save(reservation);
        System.out.println("AFTER SAVE: " + saved);
        return saved;
    }

    @Override
    public List<TableReservation> findByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    @Override
    public List<TableReservation> findAllAvailable() {
        return reservationRepository.findAvailable();
    }
	

    @Override
    public boolean existsByCustomerIdAndDateAndTime(String customerId, LocalDate date, LocalTime time) {
		return false;
//        return reservationRepository
//            .existsByCustomerIdAndReservationDateAndReservationTime(customerId, date, time);
    }

	@Override
	public TableDefination save(TableDefination table) {
		// TODO Auto-generated method stub
		return null;
	}
}