package tableservice.repository;

import java.time.LocalDateTime;

import tableservice.entity.TableReservationResource;

public interface CreateTableReservationUseCase {
    TableReservationResource createReservation(String customerId, Long tableId, LocalDateTime slotStart);
}