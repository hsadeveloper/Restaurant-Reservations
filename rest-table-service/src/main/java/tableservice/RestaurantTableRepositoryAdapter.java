package tableservice;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Repository;

import tableservice.domain.TableReservation;

@Repository
public class RestaurantTableRepositoryAdapter implements RestaurantTableRepositoryPort {

    private final TableReservationRepository reservationRepository;

    public RestaurantTableRepositoryAdapter(TableReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public TableReservation save(TableReservation table) {
        System.out.println("Saving..." + table);
        TableReservation saved = reservationRepository.save(table);
        System.out.println("AFTER SAVE: " + saved);
        return saved;
    }

    @Override
    public List<TableReservation> findAllAvailable() {
        return reservationRepository.findAvailable();
    }

    @Override
    public List<TableReservation> findByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

   

	public boolean existsByCustomerIdAndDateAndTime(String customerId, LocalDate date, LocalTime time) {
		
		return reservationRepository.existsByCustomerIdAndReservationDateAndReservationTime(customerId, date, time);
	}
}
