package orderservice.entity;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.xml.bind.ValidationException;

public class ReservationService {
    private static final int MIN_ADVANCE_MINUTES = 30;
    private static final int PENDING_EXPIRY_MINUTES = 60;
    private static final int BOOKING_START_HOUR = 11;
    private static final int BOOKING_END_HOUR = 22;
    private static final int MAX_PARTY_SIZE = 6;
    private static final int OVERLAP_BUFFER_MINUTES = 60;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableServiceClient tableServiceClient;

    /**
     * Business Rule 1: Auto-cancel pending reservations after 1 hour
     * Business Rule 2: Validate booking window (11:00–22:00)
     * Business Rule 3: Must be at least 30 minutes in the future
     * Business Rule 4: Party size must not exceed table capacity (≤ 6)
     * Business Rule 5: No overlapping reservations by same customer within an hour
     */
    public ReservationResponse createReservation(CreateReservationRequest request) {
        // Validate request
        validateReservationRequest(request);

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setCustomerId(request.getCustomerId());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setPartySize(request.getPartySize());
        reservation.setTableId(request.getTableId());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation saved = reservationRepository.save(reservation);
        log.info("Created pending reservation: {}", saved.getId());

        return toReservationResponse(saved);
    }

    public ReservationResponse confirmReservation(Long reservationId, String customerId) {
        Reservation reservation = reservationRepository.findByIdAndCustomerId(reservationId, customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new InvalidOperationException("Only pending reservations can be confirmed");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation saved = reservationRepository.save(reservation);
        log.info("Confirmed reservation: {}", reservationId);

        return toReservationResponse(saved);
    }

    public ReservationResponse getReservation(Long reservationId, String customerId) {
        Reservation reservation = reservationRepository.findByIdAndCustomerId(reservationId, customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        return toReservationResponse(reservation);
    }

    /**
     * Scheduled task to auto-cancel pending reservations after 1 hour
     */
    @Scheduled(fixedDelay = 300000) // Run every 5 minutes
    public void autoCancelExpiredPendingReservations() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(PENDING_EXPIRY_MINUTES);
        List<Reservation> expiredReservations = reservationRepository
            .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, threshold);

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.AUTO_CANCELED);
            reservationRepository.save(reservation);
            log.info("Auto-canceled reservation: {} (created at: {})", 
                reservation.getId(), reservation.getCreatedAt());
        }
    }

    // ============ VALIDATIONS ============

    private void validateReservationRequest(CreateReservationRequest request) {
        // Validate 30 minutes in the future (Rule 3)
        LocalDateTime minBookingTime = LocalDateTime.now().plusMinutes(MIN_ADVANCE_MINUTES);
        if (request.getReservationTime().isBefore(minBookingTime)) {
            throw new ValidationException(
                String.format("Reservation must be at least %d minutes in the future", MIN_ADVANCE_MINUTES));
        }

        // Validate booking window 11:00–22:00 (Rule 2)
        int hour = request.getReservationTime().getHour();
        if (hour < BOOKING_START_HOUR || hour >= BOOKING_END_HOUR) {
            throw new ValidationException(
                String.format("Bookings only available between %02d:00 and %02d:00", 
                    BOOKING_START_HOUR, BOOKING_END_HOUR));
        }

        // Fetch table and validate party size (Rule 4)
        TableDTO table = tableServiceClient.getTableById(request.getTableId());
        if (request.getPartySize() > table.getCapacity() || request.getPartySize() > MAX_PARTY_SIZE) {
            throw new ValidationException(
                String.format("Party size exceeds table capacity or max limit of %d", MAX_PARTY_SIZE));
        }

        // Validate no overlapping reservations within an hour (Rule 5)
        validateNoOverlappingReservations(request.getCustomerId(), request.getReservationTime());
    }

    private void validateNoOverlappingReservations(String customerId, LocalDateTime reservationTime) {
        LocalDateTime bufferStart = reservationTime.minusMinutes(OVERLAP_BUFFER_MINUTES);
        LocalDateTime bufferEnd = reservationTime.plusMinutes(OVERLAP_BUFFER_MINUTES);

        List<Reservation> overlappingReservations = reservationRepository
            .findByCustomerIdAndStatusAndReservationTimeBetween(
                customerId,
                ReservationStatus.CONFIRMED,
                bufferStart,
                bufferEnd
            );

        if (!overlappingReservations.isEmpty()) {
            throw new ValidationException(
                "Customer has a confirmed reservation within 1 hour of this time");
        }
    }

    // ============ HELPERS ============

    private ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getCustomerId(),
            reservation.getReservationTime(),
            reservation.getPartySize(),
            reservation.getTableId(),
            reservation.getStatus(),
            reservation.getCreatedAt()
        );
    }
}