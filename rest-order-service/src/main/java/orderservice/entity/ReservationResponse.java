package orderservice.entity;

import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;
    private String customerId;
    private LocalDateTime reservationTime;
    private Integer partySize;
    private Long tableId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
}