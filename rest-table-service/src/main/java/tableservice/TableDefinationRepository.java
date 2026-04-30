package tableservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tableservice.domain.TableDefination;

@Repository
public interface TableDefinationRepository extends JpaRepository<TableDefination, Long> {
	
	// Find tables that can fit the party size and are available
    @Query("SELECT t FROM TableDefination t WHERE t.capacity >= :partySize AND t.status = 'AVAILABLE'")
    List<TableDefination> findAvailableByCapacity(int partySize);

    // Find the best fit — smallest table that fits the party
    @Query("SELECT t FROM TableDefination t WHERE t.capacity >= :partySize AND t.status = 'AVAILABLE' ORDER BY t.capacity ASC")
    List<TableDefination> findBestFitByCapacity(int partySize);
}