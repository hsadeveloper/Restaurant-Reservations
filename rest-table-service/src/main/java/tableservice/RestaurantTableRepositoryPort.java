package tableservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import tableservice.domain.RestaurantTable;
import tableservice.domain.RestaurantTableEntity;

public interface RestaurantTableRepositoryPort {
    RestaurantTable save(RestaurantTable table);
    Optional<RestaurantTable> findById(Long id);
    List<RestaurantTable> findAllAvailable();
    List<RestaurantTableEntity> findAll();;
	Long countActiveReservationsForCustomer(String customerId, LocalDate date);
}