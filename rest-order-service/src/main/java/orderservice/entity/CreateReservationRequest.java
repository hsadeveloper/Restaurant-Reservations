package orderservice.entity;

import java.time.LocalDateTime;

public class CreateReservationRequest {
    private String customerId;
    private LocalDateTime reservationTime;
    private Integer partySize;
    private Long tableId;
}