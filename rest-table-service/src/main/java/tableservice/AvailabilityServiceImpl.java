package tableservice;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tableservice.entity.Availability;
import tableservice.entity.Table;
import tableservice.repository.ReservationRepository;
import tableservice.repository.TableRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    // 15-minute turnover buffer
    private static final Duration BUFFER_TIME = Duration.ofMinutes(15);

    public AvailabilityServiceImpl(TableRepository tableRepository,
                                   ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public List<Availability> checkAvailability(LocalDateTime dateTime, int partySize) {
        LocalDateTime startTime = dateTime.minus(BUFFER_TIME);
        LocalDateTime endTime = dateTime.plusHours(2).plus(BUFFER_TIME); // Assume 2-hour dining

        // 1. Get all tables that fit the party size
        List<Table> suitableTables = tableRepository.findByCapacityGreaterThanEqual(partySize);
        List<Availability> results = new ArrayList<>();

        // 2. Check each table for conflicts
        for (Table table : suitableTables) {
            boolean hasConflict = reservationRepository.existsByTableIdAndTimeOverlap(
                    table.getId(), startTime, endTime);

            if (!hasConflict) {
                results.add(new Availability(
                        table.getId(),
                        table.getCapacity(),
                        dateTime,
                        dateTime.plusHours(2),
                        true,
                        null
                ));
            } else {
                results.add(new Availability(
                        table.getId(),
                        table.getCapacity(),
                        dateTime,
                        dateTime.plusHours(2),
                        false,
                        "Table is already booked"
                ));
            }
        }

        return results;
    }
}
