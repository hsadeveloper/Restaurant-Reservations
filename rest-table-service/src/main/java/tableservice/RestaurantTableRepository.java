package tableservice;

import org.springframework.data.jpa.repository.JpaRepository;

import tableservice.domain.ReservationStatus;
import tableservice.domain.RestaurantTableEntity;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTableEntity, Long> {
    List<RestaurantTableEntity> findByStatus(String status);

	List<RestaurantTableEntity> findByStatus(ReservationStatus available);
}

