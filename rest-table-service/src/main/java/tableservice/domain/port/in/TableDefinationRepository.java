package tableservice.domain.port.in;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tableservice.domain.TableDefination;

@Repository
public interface TableDefinationRepository extends JpaRepository<TableDefination, Long> {

  @Query("""
          SELECT ta.table
          FROM TableAvailability ta
          WHERE ta.status = 'AVAILABLE'
            AND ta.table.capacity >= :partySize
      """)
  List<TableDefination> findBestFitByCapacity(@Param("partySize") int partySize);

}
