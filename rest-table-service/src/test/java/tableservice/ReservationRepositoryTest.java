package tableservice;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.adapter.out.persistence.TableAvailabilityRepository;
import tableservice.api.TableDefinitionDTO;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Testcontainers
@Transactional
class TableAvailabilityControllerTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine");

  @Autowired
  private TableAvailabilityRepository tableAvailabilityRepository;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PersistenceContext
  private EntityManager entityManager;

  @BeforeEach
  void setUp() {
    entityManager.flush();
    entityManager.clear();
    jdbcTemplate.execute("TRUNCATE TABLE table_availability RESTART IDENTITY CASCADE;");
  }

  @Test
  void shouldGetAllAvailableTables() { // <-- ADDED: Missing method signature declaration line
    // Arrange
    List<TableAvailability> tables = List.of(
        new TableAvailability("CUST-101", 4, ReservationStatus.AVAILABLE, LocalDate.of(2026, 7, 4),
            LocalTime.of(19, 0)),
        new TableAvailability("CUST-102", 2, ReservationStatus.AVAILABLE, LocalDate.of(2026, 7, 4),
            LocalTime.of(20, 30)));
    tableAvailabilityRepository.saveAllAndFlush(tables);

    // Act
    ResponseEntity<List<TableDefinitionDTO>> response = restTemplate.exchange("/api/tables/all",
        HttpMethod.GET, null, new ParameterizedTypeReference<List<TableDefinitionDTO>>() {});

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TableDefinitionDTO> body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body).hasSize(6);

    // Match against your DTO's specific getters (.getCapacity() or .capacity())
    assertThat(body.get(0).getCapacity()).isEqualTo(4);
  } // <-- CLOSED: Properly wraps the test logic
}
