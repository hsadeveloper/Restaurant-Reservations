package tableservice;

import java.time.LocalDateTime;
import java.util.List;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;
import tableservice.domain.ReservationStatus;

public interface AvailabilityRepositoryPort {

  List<ReservationResponse> findByStatus();

  TableAvailability save(TableAvailability table);

  List<TableAvailability> findBestFitCapacity(int size);

  List<ReservationResponse> checkAvailability();

  List<TableAvailability> findByStatusAndCreatedAtBefore(ReservationStatus status,
      LocalDateTime time);



}
