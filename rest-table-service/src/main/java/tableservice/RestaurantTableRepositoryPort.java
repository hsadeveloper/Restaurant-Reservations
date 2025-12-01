package tableservice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import tableservice.domain.RestaurantTable;
import tableservice.domain.TableAvailability;
import tableservice.domain.TableDefination;
import tableservice.domain.TableReservation;

public interface RestaurantTableRepositoryPort {
	TableReservation save(TableReservation table);
    List<TableReservation> findAllAvailable();
    List<TableReservation> findByStatus(String status);
    //boolean existsByCustomerIdAndDateAndTime(String customerId, LocalDate date, LocalTime time);
 
    
}