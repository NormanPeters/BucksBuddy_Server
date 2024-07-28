package com.bucksbuddy.bucksbuddy.expenditure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure, Integer> {
    Optional<Expenditure> findById(int id);

    List<Expenditure> findAllByJourneyId(int journeyId);
}
