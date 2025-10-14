package tableservice.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tableservice.AvailabilityService;
import tableservice.entity.Availability;
import tableservice.entity.AvailabilityResource;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final AvailabilityService availabilityService;

    public TableController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    /**
     * Check table availability with HATEOAS links
     * POST /tables/availability?date=2025-10-15&time=19:30&partySize=4
     */
    @PostMapping("/availability")
    public ResponseEntity<CollectionModel<AvailabilityResource>> checkAvailability(
            @RequestParam("date") LocalDate date,
            @RequestParam("time") LocalTime time,
            @RequestParam("partySize") Integer partySize) {
    	
    	System.out.println("Inside   "+date+"  "+time +"  "+" "+partySize );

        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Assuming this returns List<Availability>
        List<Availability> availabilities = availabilityService.checkAvailability(dateTime, partySize);

        // Map to HATEOAS resources
        List<AvailabilityResource> resources = availabilities.stream()
                .map(this::toAvailabilityResource)
                .toList();

        // Wrap in CollectionModel with extra links
        CollectionModel<AvailabilityResource> model = CollectionModel.of(resources);
        model.add(Link.of(String.format("/tables/availability?date=%s&time=%s&partySize=%d",
                date, time, partySize), "self"));
        model.add(Link.of("/tables/policies", "policies"));

        return ResponseEntity.ok(model);
    }

    /**
     * Convert Availability to HATEOAS resource
     */
    private AvailabilityResource toAvailabilityResource(Availability availability) {
        AvailabilityResource resource = new AvailabilityResource(
                availability.getTableId(),
                availability.getCapacity(),
                availability.getSlotStart(),
                availability.getSlotEnd(),
                availability.getAvailable(),
                availability.getReason()
        );

        if (availability.getAvailable()) {
            resource.add(Link.of(String.format(
                    "/tables/reservations?tableId=%d&slotStart=%s",
                    availability.getTableId(),
                    availability.getSlotStart()),
                    "reserve"));
        }

        return resource;
    }
}
