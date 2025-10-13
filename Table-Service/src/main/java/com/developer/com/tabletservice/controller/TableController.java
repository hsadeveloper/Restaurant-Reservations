package com.developer.com.tabletservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.developer.com.tabletservice.Reservation;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {

    private final CheckAvailabilityUseCase availabilityUseCase;
    private final CreateReservationUseCase reservationUseCase;
    private final GetPoliciesUseCase policiesUseCase;

    @PostMapping("/availability")
    public CollectionModel<EntityModel<Table>> checkAvailability(
        @RequestParam String date,
        @RequestParam String time,
        @RequestParam int partySize) {

        List<Table> available = availabilityUseCase.checkAvailability(
            LocalDate.parse(date), LocalTime.parse(time), partySize);

        List<EntityModel<Table>> tableModels = available.stream()
            .map(table -> EntityModel.of(table,
                linkTo(methodOn(TableController.class).checkAvailability(date, time, partySize)).withSelfRel(),
                linkTo(methodOn(TableController.class).createReservation(null)).withRel("reserve")
            ))
            .collect(Collectors.toList());

        return CollectionModel.of(tableModels);
    }

    
    
    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        Reservation saved = reservationUseCase.createReservation(reservation);
        return ResponseEntity.created(
            linkTo(methodOn(TableController.class).createReservation(reservation)).toUri())
            .body(saved);
    }

    @GetMapping("/policies")
    public List<String> getPolicies() {
        return policiesUseCase.getAllPolicies();
    }
}

