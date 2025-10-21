

//domain/port/out/ReservationRepository.java
package tableservice.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import tableservice.domain.TableReservation;

public interface ReservationRepository {
  TableReservation save(TableReservation reservation);

  TableReservation  findById(Long id);

  TableReservation create(TableReservation reservation);
  
  List<TableReservation> findAll();  // This will return a list of TableReservation objects

   boolean existsByTableIdAndStatusAndExpiresAtAfter(Long id, String string, LocalDateTime now);
  
}
