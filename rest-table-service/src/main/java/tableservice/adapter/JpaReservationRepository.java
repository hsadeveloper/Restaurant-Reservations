package tableservice.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import tableservice.adapter.in.web.ReservationController;
import tableservice.adapter.out.persistence.ReservationEntity;
import tableservice.adapter.out.persistence.SpringDataReservationRepository;
import tableservice.domain.model.TableReservation;
import tableservice.domain.port.out.ReservationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class JpaReservationRepository implements ReservationRepository {

    private final SpringDataReservationRepository jpaRepo;
    
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    public JpaReservationRepository(SpringDataReservationRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public TableReservation save(TableReservation reservation) {
        // Convert the domain model to a JPA entity
        ReservationEntity entity = new ReservationEntity();
        entity.setStatus(reservation.getStatus());  // Set status from domain model
        entity.setExpiresAt(reservation.getExpiresAt());  // Set expiresAt from domain model
        //entity.setTableNumber(reservation.getTableNumber());  // Assuming there's a tableNumber

        // Save the entity to the database and retrieve the saved entity
        ReservationEntity savedEntity = jpaRepo.save(entity);

        // Set the generated ID from the entity into the domain model
        reservation.setId(savedEntity.getId());

        return reservation;
    }

    @Override
    public TableReservation findById(Long id) {
        // Use JpaRepository's findById method to get an Optional<ReservationEntity>
        Optional<ReservationEntity> optionalEntity = jpaRepo.findById(id);

        // If the entity is found, map it to a TableReservation domain model
        if (optionalEntity.isPresent()) {
            ReservationEntity entity = optionalEntity.get();
            TableReservation reservation = new TableReservation();
            reservation.setId(entity.getId());
            reservation.setStatus(entity.getStatus());
            reservation.setExpiresAt(entity.getExpiresAt());
            //reservation.setTableNumber(entity.getTableNumber());  // Set the table number

            return reservation;
        }

        // If the reservation is not found, return null or handle as needed
        return null;  // Or throw an exception if necessary
    }

    @Override
    public TableReservation create(TableReservation reservation) {
        // Convert the domain model (TableReservation) to the entity model (ReservationEntity)
        ReservationEntity entity = new ReservationEntity();
        entity.setStatus(reservation.getStatus());  // Set the status from TableReservation
        entity.setExpiresAt(reservation.getExpiresAt());  // Set expiresAt from TableReservation
        // If you have any other fields to map, set them here, e.g., entity.setTableNumber(reservation.getTableNumber());

        // Save the entity to the database and get the saved entity
        ReservationEntity savedEntity = jpaRepo.save(entity);

        // Set the generated ID from the database in the TableReservation domain model
        reservation.setId(savedEntity.getId());

        // Return the domain model with the updated ID
        return reservation;
    }


	public List<TableReservation> findAll() {
        // Retrieve all ReservationEntity objects from the database
        List<ReservationEntity> reservationEntities = jpaRepo.findAll();

        // Convert the entities to TableReservation objects
        return reservationEntities.stream()
            .map(entity -> {
                TableReservation reservation = new TableReservation();
                reservation.setId(entity.getId());
                reservation.setStatus(entity.getStatus());  // Set status from entity
                reservation.setExpiresAt(entity.getExpiresAt());  // Set expiresAt from entity
                // reservation.setTableNumber(entity.getTableNumber());  // Uncomment if you need table number
                return reservation;
            })
            .collect(Collectors.toList());
    }
}
