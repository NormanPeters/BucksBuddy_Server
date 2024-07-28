package com.bucksbuddy.bucksbuddy.expenditure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/journeys/{journeyId}/expenditures")
public class ExpenditureController {

    @Autowired
    private ExpenditureService expenditureService;

    @GetMapping
    public ResponseEntity<List<Expenditure>> getAllExpendituresByJourneyId(@PathVariable int journeyId) {
        List<Expenditure> expenditures = expenditureService.getAllExpendituresByJourneyId(journeyId);
        return ResponseEntity.ok(expenditures);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expenditure> getExpenditureById(@PathVariable int id) {
        Optional<Expenditure> expenditure = expenditureService.getExpenditureById(id);
        return expenditure.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Expenditure> createExpenditure(@PathVariable int journeyId, @RequestBody Expenditure expenditure) {
        Expenditure createdExpenditure = expenditureService.createExpenditure(journeyId, expenditure);
        return new ResponseEntity<>(createdExpenditure, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenditure(@PathVariable int id) {
        boolean isDeleted = expenditureService.deleteExpenditure(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expenditure> updateExpenditure(@PathVariable int id, @RequestBody Expenditure expenditure) {
        Optional<Expenditure> updatedExpenditure = expenditureService.updateExpenditure(id, expenditure);
        return updatedExpenditure.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
