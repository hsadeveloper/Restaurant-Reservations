package orderservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import orderservice.entity.ReservationResponse;
import orderservice.service.ReservationService;


@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationServiceTest {



  private ReservationService reservationService;



  public ReservationServiceTest(ReservationService reservationService) {
    super();
    this.reservationService = reservationService;
  }

  @Container
  public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
      .withDatabaseName("restaurants").withUsername("postgres").withPassword("password");


  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
  }

  @Test
  void sampleTest() {
    System.out.println("JDBC URL: " + postgresContainer.getJdbcUrl());
    System.out.println("User: " + postgresContainer.getUsername());
    System.out.println("Password: " + postgresContainer.getPassword());
  }

  @Test
  void shouldCreateReservationSuccessfully() {
    // Given
    LocalDate date = LocalDate.now(ZoneId.of("America/Chicago")).plusDays(1); // 1 day ahead
    LocalTime time = LocalTime.of(19, 0); // 7:00 PM
    CreateReservationRequest request = new CreateReservationRequest(date, time, 4, "C-23");

    ReservationResponse fakeResponse = new ReservationResponse();

    // When
    ReservationResponse response = reservationService.createReservation(request);

    // Then
    assertNotNull(response);

  }
}
