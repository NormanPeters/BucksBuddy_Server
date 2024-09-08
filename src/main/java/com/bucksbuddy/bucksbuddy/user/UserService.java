package com.bucksbuddy.bucksbuddy.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsersByUsername(String username) {
        return userRepository.findAllByUsername(username);
    }

    public Optional<User> getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String uuid) {
        getUserByUuid(uuid).ifPresent(user -> userRepository.delete(user));
    }

    public Optional<User> updateUserPassword(String uuid, String newPassword) {
        Optional<User> userOpt = getUserByUuid(uuid);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
