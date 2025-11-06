package tableservice;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import tableservice.domain.RestaurantTable;
import tableservice.domain.RestaurantTableEntity;

@Repository
public class RestaurantTableRepositoryAdapter implements RestaurantTableRepositoryPort {

 private final RestaurantTableRepository jpaRepository;

 public RestaurantTableRepositoryAdapter(RestaurantTableRepository jpaRepository) {
     this.jpaRepository = jpaRepository;
 }

 @Override
 public RestaurantTable save(RestaurantTable table) {

     RestaurantTableEntity entity = new RestaurantTableEntity(
         table.getCustomerId(),
         table.getCapacity(),
         table.getStatus(),
         table.getReservationDate(),
         table.getReservationTime()
     );
     System.out.println("Inside Repos  "+table.toString());
     RestaurantTableEntity saved = jpaRepository.save(entity);
     
     System.out.println("Inside Repos  after "+saved.toString());
     return new RestaurantTable(
         saved.getId(),
         saved.getStatus(),
         saved.getCustomerId(),
         saved.getCapacity()
     );
 }

 @Override
 public Optional<RestaurantTable> findById(Long id) {
     return jpaRepository.findById(id)
             .map(e -> new RestaurantTable(e.getId(), e.getStatus(), e.getCustomerId(), e.getCapacity()));
 }

 @Override
 public List<RestaurantTable> findAllAvailable() {
	 System.out.println("findAllAvailable ......");
     return jpaRepository.findByStatus("AVAILABLE")
             .stream()
             .map(e -> new RestaurantTable(e.getId(), e.getStatus(), e.getCustomerId(), e.getCapacity()))
             .collect(Collectors.toList());
 }

@Override
public List<RestaurantTableEntity> findAll() {
	System.out.println("findAll  ......");
    return jpaRepository.findAll();

}

@Override
public Long countActiveReservationsForCustomer(String customerId, LocalDate date) {
	return jpaRepository.countActiveReservationsForCustomerOnDate( customerId,  date);
	 
}



}
