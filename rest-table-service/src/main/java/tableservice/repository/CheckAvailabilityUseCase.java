package tableservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import tableservice.entity.Availability;

public interface CheckAvailabilityUseCase {
    List<Availability> checkAvailability(LocalDateTime dateTime, Integer partySize);
}
