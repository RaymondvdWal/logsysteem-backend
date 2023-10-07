package com.example.logsysteem.utils;

import org.springframework.validation.BindingResult;

public class FieldError {

    public FieldError() {
    }

    public String fieldErrorBuilder(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (org.springframework.validation.FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField()).append(": ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return sb.toString();
    }
}
