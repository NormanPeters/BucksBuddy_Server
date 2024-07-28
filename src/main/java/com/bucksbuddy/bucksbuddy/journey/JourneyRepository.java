package com.bucksbuddy.bucksbuddy.journey;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyRepository extends CrudRepository<Journey, Integer> {
    List<Journey> findAllByUser_Uuid(String uuid); // Ändere das Mapping entsprechend der Feldnamen in der User-Entity

    Optional<Journey> findById(int id);
}
