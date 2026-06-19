package tableservice;

import java.util.List;
import org.springframework.http.ResponseEntity;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;

public interface AvailabilityRepositoryPort {

  List<ReservationResponse> findByStatus();

  TableAvailability save(TableAvailability table);

  List<TableAvailability> findBestFitCapacity(int size);

  ResponseEntity<ReservationResponse> checkAvailability();



}
