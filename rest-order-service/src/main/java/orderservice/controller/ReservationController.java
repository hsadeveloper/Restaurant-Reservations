package orderservice.controller;

// 1. FIX: Add the missing static method imports for Spring HATEOAS link builder
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List; // FIX: Add the missing collection List utility import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import orderservice.entity.ReservationRequestDTO;
import orderservice.entity.ReservationResponse;
import orderservice.entity.RestaurantTableEntity;
import orderservice.service.ReservationService;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

  private ReservationService reservationService;
  private final ObjectMapper mapper;
  private StringRedisTemplate stringRedisTemplate;

  // Ensure your constructor looks EXACTLY like this
  public ReservationController(ReservationService reservationService,
      StringRedisTemplate stringRedisTemplate) {
    super();
    this.reservationService = reservationService;
    this.stringRedisTemplate = stringRedisTemplate;

    // CRITICAL FIX: Instantiate the mapper and register the time module
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    // Optional: Prevent crashes if the subscriber JSON has fields the controller doesn't use
    this.mapper.configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }


  @GetMapping("/latest")
  public ResponseEntity<ReservationRequestDTO[]> getLatestAvailableTables() {
    try {
      // 1. Fetch the synced JSON block created by your TableSubscriber
      String jsonPayload = stringRedisTemplate.opsForValue().get("availableTables::latest");

      if (jsonPayload == null) {
        // If the 15-minute TTL cache window expired, return an empty array
        return ResponseEntity.ok(new ReservationRequestDTO[0]);
      }

      // 2. Parse the string value back into a readable DTO array
      ReservationRequestDTO[] tables = mapper.readValue(jsonPayload, ReservationRequestDTO[].class);

      // 3. Return the array with an HTTP 200 status code
      return ResponseEntity.ok(tables);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }



  @PostMapping("/")
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody ReservationRequestDTO request) {
    ReservationResponse response = reservationService.createReservation(request);
    response.add(Link.of("/tables/reservations").withRel("reserve"));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/{id}/confirm")
  public ResponseEntity<EntityModel<ReservationResponse>> confirmReservation(
      @PathVariable("id") Long id) {
    ReservationResponse response = reservationService.confirm(id);
    EntityModel<ReservationResponse> model = EntityModel.of(response,
        linkTo(methodOn(ReservationController.class).confirmReservation(id)).withRel("confirm"));
    return ResponseEntity.ok(model);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<RestaurantTableEntity>> getReservation(
      @PathVariable("id") Long id) {
    RestaurantTableEntity response = reservationService.getReservation(id);
    EntityModel<RestaurantTableEntity> model = EntityModel.of(response,
        linkTo(methodOn(ReservationController.class).confirmReservation(id)).withRel("confirm"));
    return ResponseEntity.ok(model);
  }

  @GetMapping("/all")
  public ResponseEntity<CollectionModel<RestaurantTableEntity>> getAllReservation() {
    List<RestaurantTableEntity> response = reservationService.getAllReservation();
    CollectionModel<RestaurantTableEntity> model = CollectionModel.of(response,
        linkTo(methodOn(ReservationController.class).getAllReservation()).withSelfRel());
    return ResponseEntity.ok(model);
  }
}
