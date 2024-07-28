package com.bucksbuddy.bucksbuddy.journey;

import com.bucksbuddy.bucksbuddy.user.User;
import com.bucksbuddy.bucksbuddy.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users/journeys")
public class JourneyController {

    @Autowired
    JourneyService journeyService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Iterable<Journey>> getAllJourneys(@RequestHeader("uuid") String uuid) {
        Iterable<Journey> journeys = journeyService.getAllJourneys(uuid);
        return ResponseEntity.ok(journeys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Journey> getJourneyById(@PathVariable int id) {
        Optional<Journey> journey = journeyService.getJourneyById(id);
        return journey.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Journey> createJourney(@RequestHeader("uuid") String uuid, @RequestBody Journey journey) {
        Optional<User> userOptional = userService.getUserByUuid(uuid);
        if (userOptional.isPresent()) {
            journey.setUser(userOptional.get());
            Journey createdJourney = journeyService.createJourney(uuid, journey);
            return new ResponseEntity<>(createdJourney, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJourney(@PathVariable int id) {
        boolean isDeleted = journeyService.deleteJourney(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Journey> updateJourneyName(@PathVariable int id, @RequestBody Map<String, String> updates) {
        Optional<Journey> updatedJourney = journeyService.updateJourneyName(id, updates.get("name"));
        return updatedJourney.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/homeCurrency")
    public ResponseEntity<String> getHomeCurrency(@PathVariable int id) {
        Optional<String> currency = journeyService.getHomeCurrency(id);
        return currency.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/vacCurrency")
    public ResponseEntity<String> getVacCurr(@PathVariable int id) {
        Optional<String> vacCurr = journeyService.getVacCurr(id);
        return vacCurr.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/budget")
    public ResponseEntity<Integer> getJourneyBudget(@PathVariable int id) {
        Optional<Integer> budget = journeyService.getJourneyBudget(id);
        return budget.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/startDate")
    public ResponseEntity<String> getStartDate(@PathVariable int id) {
        Optional<String> startDate = journeyService.getStartDate(id);
        return startDate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/endDate")
    public ResponseEntity<String> getEndDate(@PathVariable int id) {
        Optional<String> endDate = journeyService.getEndDate(id);
        return endDate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
