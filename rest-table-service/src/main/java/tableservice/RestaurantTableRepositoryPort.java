package tableservice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import tableservice.domain.TableDefination;
import tableservice.domain.TableReservation;

public interface RestaurantTableRepositoryPort {
    TableDefination save(TableDefination table);
    TableReservation save(TableReservation reservation);
    List<TableReservation> findAllAvailable();
    List<TableReservation> findByStatus(String status);
    boolean existsByCustomerIdAndDateAndTime(String customerId, LocalDate date, LocalTime time);
}