package com.bucksbuddy.bucksbuddy.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmail(String email);
}
