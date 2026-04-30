package tableservice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import tableservice.domain.TableDefination;
import tableservice.domain.TableReservation;

@Repository
public class RestaurantTableRepositoryAdapter implements RestaurantTableRepositoryPort {

    private final TableReservationRepository reservationRepository;
    private final TableDefinationRepository tableDefinationRepository;

    public RestaurantTableRepositoryAdapter(
        TableReservationRepository reservationRepository,
        TableDefinationRepository tableDefinationRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.tableDefinationRepository = tableDefinationRepository;
    }

    @Override
    public TableDefination save(TableDefination table) {
        System.out.println("Saving table definition..." + table);
        TableDefination saved = tableDefinationRepository.save(table);
        System.out.println("AFTER SAVE: " + saved);
        return saved;
    }

    @Override
    public TableReservation save(TableReservation reservation) {
        System.out.println("Saving reservation..." + reservation);
        TableReservation saved = reservationRepository.save(reservation);
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

	

    @Override
    public boolean existsByCustomerIdAndDateAndTime(String customerId, LocalDate date, LocalTime time) {
        return reservationRepository
            .existsByCustomerIdAndReservationDateAndReservationTime(customerId, date, time);
    }
}