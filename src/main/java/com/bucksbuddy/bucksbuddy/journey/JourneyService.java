package com.bucksbuddy.bucksbuddy.journey;

import com.bucksbuddy.bucksbuddy.user.User;
import com.bucksbuddy.bucksbuddy.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JourneyService {

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Journey> getAllJourneys(String uuid) {
        System.out.println("Fetching journeys for UUID: " + uuid);
        List<Journey> journeys = journeyRepository.findAllByUser_Uuid(uuid);
        System.out.println("Journeys found: " + journeys.size());
        return journeys;
    }

    public Optional<Journey> getJourneyById(int id) {
        return journeyRepository.findById(id);
    }

    public Journey createJourney(String uuid, Journey journey) {
        Optional<User> user = userRepository.findByUuid(uuid);
        user.ifPresent(journey::setUser);
        return journeyRepository.save(journey);
    }

    public boolean deleteJourney(int id) {
        if (journeyRepository.existsById(id)) {
            journeyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Journey> updateJourneyName(int id, String name) {
        return journeyRepository.findById(id).map(journey -> {
            journey.setName(name);
            return journeyRepository.save(journey);
        });
    }

    public Optional<String> getHomeCurrency(int id) {
        return journeyRepository.findById(id).map(Journey::getHomeCurr);
    }

    public Optional<Integer> getJourneyBudget(int id) {
        return journeyRepository.findById(id).map(Journey::getBudget);
    }

    public Optional<String> getVacCurr(int id) {
        return journeyRepository.findById(id).map(Journey::getVacCurr);
    }

    public Optional<String> getStartDate(int id) {
        return journeyRepository.findById(id).map(Journey::getStartDate);
    }

    public Optional<String> getEndDate(int id) {
        return journeyRepository.findById(id).map(Journey::getEndDate);
    }
}
