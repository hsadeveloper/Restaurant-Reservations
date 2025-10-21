package tableservice.application.port.in;

import java.util.List;
import java.util.Optional;

import tableservice.domain.TableEntity;
import tableservice.domain.TableReservation;


public interface TableRepositoryPort {
    Optional<TableEntity> findById(Long id);
    List<TableEntity> findAllAvailable();

	
}