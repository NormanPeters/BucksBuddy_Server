package com.bucksbuddy.bucksbuddy.expenditure;

import com.bucksbuddy.bucksbuddy.journey.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenditureService {

    @Autowired
    private ExpenditureRepository expenditureRepository;

    @Autowired
    private JourneyRepository journeyRepository;

    public List<Expenditure> getAllExpendituresByJourneyId(int journeyId) {
        return expenditureRepository.findAllByJourneyId(journeyId);
    }

    public Optional<Expenditure> getExpenditureById(int id) {
        return expenditureRepository.findById(id);
    }

    public Expenditure createExpenditure(int journeyId, Expenditure expenditure) {
        return journeyRepository.findById(journeyId).map(journey -> {
            expenditure.setJourney(journey);
            return expenditureRepository.save(expenditure);
        }).orElseThrow(() -> new IllegalArgumentException("Journey not found"));
    }

    public boolean deleteExpenditure(int id) {
        if (expenditureRepository.existsById(id)) {
            expenditureRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Expenditure> updateExpenditure(int id, Expenditure updatedExpenditure) {
        return expenditureRepository.findById(id).map(expenditure -> {
            expenditure.setName(updatedExpenditure.getName());
            expenditure.setAmount(updatedExpenditure.getAmount());
            expenditure.setDate(updatedExpenditure.getDate());
            return expenditureRepository.save(expenditure);
        });
    }
}
