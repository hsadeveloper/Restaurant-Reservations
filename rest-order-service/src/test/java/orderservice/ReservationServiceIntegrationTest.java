package orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import orderservice.entity.CreateReservationRequest;
import orderservice.entity.ReservationResponse;
import orderservice.service.ReservationService;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
public class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void testCreateReservation() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setDate(LocalDate.now().plusDays(1));
        request.setTime(LocalTime.of(19, 0));
        request.setPartySize(4);
        request.setCustomerId("c-101");

        ReservationResponse response = reservationService.createReservation(request);

        assertThat(response).isNotNull();
      
    }
}
