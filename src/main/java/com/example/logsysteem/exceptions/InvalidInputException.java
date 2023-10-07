package com.example.logsysteem.exceptions;


import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class InvalidInputException extends BindException {
    public  InvalidInputException(BindingResult message) {
        super(message);
    }
}

