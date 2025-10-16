

//domain/port/out/ReservationRepository.java
package tableservice.domain.port.out;

import java.util.List;

import tableservice.domain.model.TableReservation;

public interface ReservationRepository {
 TableReservation save(TableReservation reservation);

  TableReservation  findById(Long id);

  TableReservation create(TableReservation reservation);
  
  List<TableReservation> findAll();  // This will return a list of TableReservation objects
  
}
