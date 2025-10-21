package orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import orderservice.entity.CreateReservationRequest;
import orderservice.entity.Reservation;
import orderservice.entity.ReservationResponse;
import orderservice.entity.TableAvailabilityRequest;
import orderservice.repository.ReservationRepository;
@Service
public class ReservationService {
    private static final int MIN_ADVANCE_MINUTES = 30;
    private static final int PENDING_EXPIRY_MINUTES = 60;
    private static final int BOOKING_START_HOUR = 11;
    private static final int BOOKING_END_HOUR = 22;
    private static final int MAX_PARTY_SIZE = 6;
    private static final int OVERLAP_BUFFER_MINUTES = 60;

    @Autowired
    private ReservationRepository reservationRepository;
    
    private final RestTemplate restTemplate;

    public ReservationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    @Autowired
//    private TableServiceClient tableServiceClient;

    /**
     * Business Rule 1: Auto-cancel pending reservations after 1 hour
     * Business Rule 2: Validate booking window (11:00–22:00)
     * Business Rule 3: Must be at least 30 minutes in the future
     * Business Rule 4: Party size must not exceed table capacity (≤ 6)
     * Business Rule 5: No overlapping reservations by same customer within an hour
     */

    
    public ReservationResponse createReservation(CreateReservationRequest request) {
    	
    	// ✅ Combine date and time to get a LocalDateTime
    	ZoneId zone = ZoneId.of("America/Chicago");

    	ZonedDateTime reservationTime = LocalDateTime.of(request.getDate(), request.getTime()).atZone(zone);
    	ZonedDateTime now = ZonedDateTime.now(zone); // ✅ make this use the same time zone
    	System.out.println("Reservation Time:     " + reservationTime);
    	System.out.println("Current Time + 30min: " + LocalDateTime.now(zone));

    	if (reservationTime.isBefore(now.plusMinutes(30))) {
    	    throw new RuntimeException("Reservations must be at least 30 minutes in the future.");
    	}

//        LocalDateTime reservationTime = LocalDateTime.of(request.getDate(), request.getTime());
        System.out.println("Request time:      " + reservationTime);
        System.out.println("System time (+30): " + LocalDateTime.now().plusMinutes(30));
        
        System.out.println("Inside createReservation   .. .............................................."+ LocalDateTime.now() );
        if (reservationTime.isBefore(now.plusMinutes(30))) {
            throw new RuntimeException("Reservations must be at least 30 minutes in the future.");
        }

        // ✅ 2. Max party size ≤ 6
        if (request.getPartySize() > MAX_PARTY_SIZE) {
        	System.out.println("Party size > 6   .. "+request.getPartySize());
            throw new RuntimeException("Maximum allowed party size per table is 6.");
        }
      
//       // ✅ 3. No overlapping reservations by same customer within 1 hour
//        LocalDateTime windowStart = reservationTime.minusHours(1);
//        LocalDateTime windowEnd = reservationTime.plusHours(1);
//
//        boolean hasOverlap = reservationRepository.existsByCustomerIdAndReservationTimeBetween(
//            request.getCustomerId(), windowStart, windowEnd);
//
//        if (hasOverlap) {
//            throw new RuntimeException("Customer already has a reservation within 1 hour of this time.");
//        }
       
     // ✅ 4. Reservation within allowed hours (11:00–22:00)
     LocalTime reservationLocalTime = reservationTime.toLocalTime();
     System.out.println("Ieservations can only be booked between 11:00 and 22:00 .. .............................................."+ reservationLocalTime);
     LocalTime startTime = LocalTime.of(BOOKING_START_HOUR, 0); // 11:00
     LocalTime endTime = LocalTime.of(BOOKING_END_HOUR, 0);     // 22:00

     if (reservationLocalTime.isBefore(startTime) || reservationLocalTime.isAfter(endTime)) {
    	 System.out.println("Ieservations can only be booked between 11:00 and 22:00 .. ..............................................");
         throw new RuntimeException("Reservations can only be booked between 11:00 and 22:00.");
     }

        
//
//        // ➕ Table assignment logic (simplified or from previous step)
//        List<TableEntity> availableTables = tableRepository.findAvailableTables(
//            request.getPartySize(), reservationTime);
//
//        if (availableTables.isEmpty()) {
//            throw new RuntimeException("No available tables for the selected time.");
//        }
//
//        TableEntity assignedTable = availableTables.get(0);
     
        // Build the URI with query params
     // Prepare the request body
     // Step 1: Combine date + time to LocalDateTime
        LocalDateTime reservationDateTime = LocalDateTime.parse(
            request.getDate() + "T" + request.getTime() // "2025-10-20T19:00"
        );

        Reservation reservation = new Reservation();
        reservation.setCustomerId(request.getCustomerId()); // assuming userId maps to customerId
        reservation.setReservationTime(request.getTime());
        reservation.setReservationDate(request.getDate());
        reservation.setPartySize(request.getPartySize());
//    Reservation  reservationObj= reservationRepository.save(reservation);
        
      //  EntityModel<YourDTO> model = EntityModel.of(yourDto);
        // model.add(linkTo(methodOn(TableController.class).reserve())
           //     .withRel("reserve")
             //   .withType("POST"));
//        
//        // Step 2: Build Reservation entity
        TableAvailabilityRequest availabilityRequest = new TableAvailabilityRequest();
        availabilityRequest.setCustomerId(request.getCustomerId()); // assuming userId maps to customerId
        availabilityRequest.setDate(request.getDate().toString());
        availabilityRequest.setTime(request.getTime().toString());
        availabilityRequest.setPartySize(request.getPartySize());
     
        //reservation.setTableId(Long.valueOf(availability.getTableId())); // Convert String to Long
        //eservation.setStatus(ReservationStatus.PENDING); // Optional — already default
        System.out.println("*************************  getCustomerId()  :...................................... "+availabilityRequest.getCustomerId()); 
        System.out.println("Build Reservation entity :............................................ "+availabilityRequest); 
//
//        // Make the POST call to Table Service (running on port 8088)
        ResponseEntity<ReservationResponse> response =
            restTemplate.postForEntity(
                "http://localhost:8080/api/tables/availability",
                availabilityRequest,
                ReservationResponse.class
            );      
        System.out.println("Creating reservation for customer from service  response  ........................................  "+response.getBody()); 
        // Extract the response
        //TableAvailabilityResponse availability = response.getBody();
//
//        return availability ;
		return null;
    }


    public Reservation getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
       return    reservation;

    }
    public List<Reservation> getAllReservation() {
    	System.out.println("Herererererererererer .............................");
        List<Reservation> reservation = reservationRepository.findAll();
       return    reservation;

    }
}
