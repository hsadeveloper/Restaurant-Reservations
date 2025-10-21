package tableservice.domain.port.in;

import java.util.List;
import java.util.Optional;

import tableservice.domain.ReservationResponse;
import tableservice.domain.TableEntity;
import tableservice.domain.TableReservation;

public interface ReservationUseCase {
    TableReservation reserveTable(TableReservation reservation);
    
//    // New method for updating a reservation
//    TableReservation updateTableReservation(Long id, TableReservation reservation);
//
//	TableReservation getReservationById(Long id);
//
//	TableReservation findById(Long id);
//
//	ReservationResponse createReservationResponse(TableReservation reservation);
//
//	ReservationResponse convertToResponse(TableReservation createdReservation);
//
//	//RestaurantTable findBestAvailableTable(List<RestaurantTable> allTables, int partySize);
//
//	RestaurantTable findAvailableTable(List<RestaurantTable> allTables, int partySize);
    
   
}
