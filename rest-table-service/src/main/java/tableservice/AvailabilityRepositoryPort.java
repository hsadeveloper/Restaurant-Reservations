package tableservice;

import java.util.List;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;

public interface AvailabilityRepositoryPort {

  List<ReservationResponse> findByStatus();

  TableAvailability save(TableAvailability table);

  List<TableAvailability> findBestFitCapacity(int size);



}
