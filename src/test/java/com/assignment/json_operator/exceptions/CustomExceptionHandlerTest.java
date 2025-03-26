package com.assignment.json_operator.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new CustomExceptionHandler();
    }

    @Test
    void testHandleInvalidRequest() {
        InvalidRequestException exception = new InvalidRequestException("Invalid request data");
        ResponseEntity<Object> response = exceptionHandler.handleInvalidRequest(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertResponse(response, "Invalid request data", HttpStatus.BAD_REQUEST);
    }

    @Test
    void testHandleDatasetNotFound() {
        DatasetNotFoundException exception = new DatasetNotFoundException("Dataset not found");
        ResponseEntity<Object> response = exceptionHandler.handleDatasetNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertResponse(response, "Dataset not found", HttpStatus.NOT_FOUND);
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<Object> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertResponse(response, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void assertResponse(ResponseEntity<Object> response, String expectedMessage, HttpStatus expectedStatus) {
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(expectedStatus.value(), body.get("status"));
        assertEquals(expectedStatus.getReasonPhrase(), body.get("error"));
        assertEquals(expectedMessage, body.get("message"));
    }
}
