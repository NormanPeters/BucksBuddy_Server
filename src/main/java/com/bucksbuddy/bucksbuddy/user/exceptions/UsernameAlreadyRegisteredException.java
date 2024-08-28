package com.bucksbuddy.bucksbuddy.user.exceptions;

public class UsernameAlreadyRegisteredException extends RuntimeException {
    public UsernameAlreadyRegisteredException(String username) {
        super("Username already registered: " + username);
    }
}
