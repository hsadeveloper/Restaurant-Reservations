package tableservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tableservice.entity.Table; // ✅ Use your actual entity

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByCapacityGreaterThanEqual(Integer partySize);
}
