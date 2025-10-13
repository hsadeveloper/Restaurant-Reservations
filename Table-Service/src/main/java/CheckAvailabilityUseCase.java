import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.developer.com.tabletservice.Table;

public interface CheckAvailabilityUseCase {
	
    List<Table> checkAvailability(LocalDate date, LocalTime time, int partySize);
}
