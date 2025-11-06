package tableservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tableservice.domain.ReservationStatus;
import tableservice.domain.RestaurantTableEntity;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTableEntity, Long> {
    List<RestaurantTableEntity> findByStatus(String status);

	List<RestaurantTableEntity> findByStatus(ReservationStatus available);
	
	   @Query("""
		        SELECT COUNT(r)
		        FROM RestaurantTableEntity r
		        WHERE r.customerId = :customerId
		          AND r.reservationDate = :date
		          AND r.status IN ('PENDING', 'CONFIRMED')
		    """)
		    long countActiveReservationsForCustomerOnDate(
		            @Param("customerId") String customerId,
		            @Param("date") LocalDate date);
}

