package com.example.logsysteem.exceptions;

public class RecordNotFoundException  extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}

