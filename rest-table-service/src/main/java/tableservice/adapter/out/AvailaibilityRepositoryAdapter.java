package tableservice.adapter.out;

import java.util.List;

import org.springframework.stereotype.Repository;

import tableservice.domain.TableAvailability;

@Repository
public class AvailaibilityRepositoryAdapter implements AvailabilityRepositoryPort {

    // Inject the JPA repository, NOT the adapter itself
    private final JpaAvailabilityRepository jpaRepository;
	
    public AvailaibilityRepositoryAdapter(JpaAvailabilityRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public List<TableAvailability> findAll() {
		return jpaRepository.findAll();
	}

	@Override
	public TableAvailability save(TableAvailability table) {
		return jpaRepository.save(table);
	}
}
