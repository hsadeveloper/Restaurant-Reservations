package tableservice.application.service;

import org.springframework.stereotype.Service;
import tableservice.domain.model.ReservationResponse;
import tableservice.domain.model.TableReservation;
import tableservice.domain.port.in.ReservationUseCase;
import tableservice.domain.port.out.ReservationRepository;

@Service
public class ReservationService implements ReservationUseCase {

    private final ReservationRepository reservationRepository;

    // Constructor injection for the repository
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public TableReservation reserveTable(TableReservation reservation) {
        // Add any domain logic here (e.g., validation, rules)
        return reservationRepository.save(reservation);
    }

    @Override
    public TableReservation updateTableReservation(Long id, TableReservation reservation) {
        // Check if reservation with ID exists
        TableReservation existingReservation = reservationRepository.findById(id);
        if (existingReservation != null) {
            // Update the existing reservation
            existingReservation.setStatus(reservation.getStatus());
            existingReservation.setExpiresAt(reservation.getExpiresAt());  // Assuming expiresAt is to be updated
            // Save the updated reservation
            return reservationRepository.save(existingReservation);
        } else {
            throw new RuntimeException("Reservation not found with ID: " + id);
        }
    }

    @Override
    public TableReservation findById(Long id) {
        // Find reservation by ID in the repository
        return reservationRepository.findById(id);
    }

    @Override
    public TableReservation getReservationById(Long id) {
        // Handle retrieval of reservation, potentially add error handling
        TableReservation reservation = reservationRepository.findById(id);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found with ID: " + id);
        }
        return reservation;
    }

	@Override
	public ReservationResponse createReservationResponse(TableReservation reservation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReservationResponse convertToResponse(TableReservation createdReservation) {
		// TODO Auto-generated method stub
		return null;
	}


   

   
}
