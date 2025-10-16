package tableservice.domain.port.in;

import tableservice.domain.model.ReservationResponse;
import tableservice.domain.model.TableReservation;

public interface ReservationUseCase {
    TableReservation reserveTable(TableReservation reservation);
    
    // New method for updating a reservation
    TableReservation updateTableReservation(Long id, TableReservation reservation);

	TableReservation getReservationById(Long id);

	TableReservation findById(Long id);

	ReservationResponse createReservationResponse(TableReservation reservation);

	ReservationResponse convertToResponse(TableReservation createdReservation);
}
