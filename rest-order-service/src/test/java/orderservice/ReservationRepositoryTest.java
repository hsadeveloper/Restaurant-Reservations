package orderservice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import orderservice.entity.ReservationStatus;
import orderservice.entity.RestaurantTableEntity;
import orderservice.repository.ReservationRepository;

@DataJpaTest(properties = {"spring.test.database.replace=NONE",
    "spring.datasource.url=jdbc:tc:postgresql:15:///springboot"})
public class ReservationRepositoryTest {

  @Autowired
  private ReservationRepository reservationRepository;

  @BeforeEach
  void initData() {
    // Create and save the sample object to the Postgres container
    RestaurantTableEntity sampleObj =
        new RestaurantTableEntity("CUST-001", LocalTime.of(19, 30), LocalDate.now().plusDays(2), 4);
    reservationRepository.save(sampleObj);
  }

  @Test
  void shouldFindSavedReservation() {
    // When
    boolean exists = reservationRepository.existsByCustomerIdAndReservationDateAndReservationTime(
        "CUST-001", LocalDate.now().plusDays(2), LocalTime.of(19, 30));

    // Then
    assertTrue(exists, "The reservation should exist in the database");
  }

  @Test
  void shouldTestStatusQuery() {
    // When
    List<RestaurantTableEntity> results = reservationRepository.findAllByStatusAndCreatedAtBefore(
        ReservationStatus.PENDING, java.time.LocalDateTime.now().plusMinutes(1));

    // Then
    assertNotNull(results);
    assertTrue(results.size() > 0);
  }

  @Test
  void shouldReturnTrueWhenReservationExists() {
    // Given - Using data initialized in @BeforeEach
    String customerId = "CUST-001";
    LocalDate date = LocalDate.now().plusDays(2);
    LocalTime time = LocalTime.of(19, 30);

    // When
    boolean exists = reservationRepository
        .existsByCustomerIdAndReservationDateAndReservationTime(customerId, date, time);

    // Then
    assertTrue(exists, "Should return true because the reservation was saved in @BeforeEach");
  }

  @Test
  void shouldReturnFalseWhenReservationDoesNotExist() {
    // Given
    String wrongCustomer = "NON-EXISTENT";
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();

    // When
    boolean exists = reservationRepository
        .existsByCustomerIdAndReservationDateAndReservationTime(wrongCustomer, date, time);

    // Then
    assertFalse(exists, "Should return false for data that was never saved");
  }

}
