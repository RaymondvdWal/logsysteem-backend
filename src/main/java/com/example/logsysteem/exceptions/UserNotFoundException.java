package com.example.logsysteem.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(username + " not found");
    }
}

