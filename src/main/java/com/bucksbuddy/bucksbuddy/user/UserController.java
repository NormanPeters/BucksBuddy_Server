package com.bucksbuddy.bucksbuddy.user;

import com.bucksbuddy.bucksbuddy.user.exceptions.EmailAlreadyRegisteredException;
import com.bucksbuddy.bucksbuddy.user.requests.PasswordUpdateRequest;
import com.bucksbuddy.bucksbuddy.user.requests.UserLoginRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173, https://bucksbuddyfrontend.onrender.com")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        Optional<User> validUser = userService.getUserByEmail(request.getEmail());
        String enteredPassword = request.getPassword();
        String storedPassword = validUser.map(User::getPassword).orElse("");
        if (validUser.isPresent() && passwordEncoder.matches(enteredPassword, storedPassword)) {
            return new ResponseEntity<>("UUID: " + validUser.get().getUuid(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (EmailAlreadyRegisteredException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader String uuid) {
        Optional<User> userOpt = userService.getUserByUuid(uuid);
        if (userOpt.isPresent()) {
            userService.deleteUser(uuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updateUserPassword(@RequestHeader String uuid, @Valid @RequestBody PasswordUpdateRequest payload, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        String newPassword = payload.getNewPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);
        Optional<User> updatedUser = userService.updateUserPassword(uuid, encodedPassword);

        if (updatedUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
