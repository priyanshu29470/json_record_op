package com.assignment.json_operator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DatasetNotFoundException extends RuntimeException {
    public DatasetNotFoundException(String message) {
        super(message);
    }
}
