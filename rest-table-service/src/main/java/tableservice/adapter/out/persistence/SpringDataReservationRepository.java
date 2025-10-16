package tableservice.adapter.out.persistence;



import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReservationRepository extends JpaRepository<ReservationEntity, Long> {
}

