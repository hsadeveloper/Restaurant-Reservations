package tableservice.application.port.in;




import org.springframework.stereotype.Repository;

import tableservice.application.port.out.SpringDataTableRepository;
import tableservice.domain.ReservationEntity;
import tableservice.domain.TableEntity;
import tableservice.domain.TableReservation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TableRepositoryAdapter implements TableRepositoryPort {

    private final SpringDataTableRepository jpaRepository;

public TableRepositoryAdapter(SpringDataTableRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
}

@Override
public Optional<TableEntity> findById(Long id) {
    return jpaRepository.findById(id)
            .map(this::mapToDomain);
}

@Override
public List<TableEntity> findAllAvailable() {
    return jpaRepository.findByStatus("AVAILABLE")
            .stream()
            .map(this::mapToDomain)
            .collect(Collectors.toList());
}


private <U> U mapToDomain(TableEntity tableentity1) {
	return new TableEntity(e.getId(), e.getCustomerId(), e.getCapacity(), e.getStatus());
}
	
}
