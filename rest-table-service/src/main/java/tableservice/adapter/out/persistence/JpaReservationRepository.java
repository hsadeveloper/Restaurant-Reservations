package tableservice.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tableservice.domain.model.TableReservation;
import tableservice.domain.port.out.ReservationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("jpaReservationRepositoryPersistence")
public class JpaReservationRepository implements ReservationRepository {

    private final SpringDataReservationRepository jpaRepo;

    @Autowired
    public JpaReservationRepository(SpringDataReservationRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public TableReservation save(TableReservation reservation) {
        // Convert domain model to entity
        ReservationEntity entity = new ReservationEntity();
        entity.setStatus(reservation.getStatus());  // Set status
        entity.setExpiresAt(reservation.getExpiresAt());  // Set expiration time

        // Save entity to the database
        ReservationEntity savedEntity = jpaRepo.save(entity);

        // Set the generated ID in the domain model
        reservation.setId(savedEntity.getId());

        return reservation;  // Return the domain model with the ID
    }

    @Override
    public TableReservation findById(Long id) {
        // Use JpaRepository's findById method to retrieve the entity
        Optional<ReservationEntity> optionalEntity = jpaRepo.findById(id);

        // If the entity exists, convert it to TableReservation and return
        if (optionalEntity.isPresent()) {
            ReservationEntity entity = optionalEntity.get();
            TableReservation reservation = new TableReservation();

            reservation.setId(entity.getId());  // Set ID
            reservation.setStatus(entity.getStatus());  // Set reservation status
            reservation.setExpiresAt(entity.getExpiresAt());  // Set expiration time

            return reservation;  // Return the converted domain model
        }

        // If the reservation is not found, handle it appropriately
        return null;  // You could throw an exception here instead: throw new RuntimeException("Reservation not found");
    }

	@Override
	public TableReservation create(TableReservation reservation) {
		
		return null;
	}
	
	// Method to retrieve all reservations
    public List<TableReservation> findAll() {
        List<ReservationEntity> reservationEntities = jpaRepo.findAll();  // Get all entities from the database
        return reservationEntities.stream()  // Convert each entity to the domain model
                .map(entity -> {
                    TableReservation reservation = new TableReservation();
                    reservation.setId(entity.getId());
                    reservation.setStatus(entity.getStatus());
                    reservation.setExpiresAt(entity.getExpiresAt());
                    //reservation.setTableNumber(entity.getTableNumber());
                    return reservation;
                })
                .collect(Collectors.toList());  // Collect the results into a list
    }
}
