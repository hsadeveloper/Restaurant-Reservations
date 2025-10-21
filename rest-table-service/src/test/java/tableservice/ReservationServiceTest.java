package tableservice;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ReservationServiceTest {

    // Step 3: Define container
    @Container
    public static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("restaurants");

    @Test
    void sampleTest() {
        System.out.println("JDBC URL: " + postgresContainer.getJdbcUrl());
        System.out.println("User: " + postgresContainer.getUsername());
        System.out.println("Password: " + postgresContainer.getPassword());
    }
}
