package tableservice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tableservice.domain.RestaurantTable;
import tableservice.domain.TableAvailability;
import tableservice.domain.TableReservation;

@Repository
public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {

    @Query("SELECT t FROM TableReservation t WHERE t.status = 'AVAILABLE'")
    List<TableReservation> findAvailable();

    List<TableReservation> findByStatus(String status);

 // Prevent duplicate reservations for the same customer/date/time
   
    
    boolean existsByCustomerIdAndReservationDateAndReservationTime(
            String customerId,
            LocalDate reservationDate,
            LocalTime reservationTime
        );

	//boolean existsByCustomerIdAndDateAndTime(String customerId, reservationDate date, reservationTime time);

}
