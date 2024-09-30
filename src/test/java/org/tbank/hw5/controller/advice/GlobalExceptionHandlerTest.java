package org.tbank.hw5.controller.advice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tbank.hw5.exception.EntityAlreadyExistsException;
import org.tbank.hw5.exception.EntityNotFoundException;
import org.tbank.hw5.exception.model.CustomErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleEntityAlreadyExistsException() {
        EntityAlreadyExistsException exception = new EntityAlreadyExistsException("Entity already exists");
        ResponseEntity<CustomErrorResponse> responseEntity = exceptionHandler.handleEntityAlreadyExistsException(exception);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Entity already exists", responseEntity.getBody().getMessage());
    }

    @Test
    public void testHandleEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        ResponseEntity<CustomErrorResponse> responseEntity = exceptionHandler.handleEntityNotFoundException(exception);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Entity not found", responseEntity.getBody().getMessage());
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new Exception("Some generic error");
        ResponseEntity<CustomErrorResponse> responseEntity = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Some generic error", responseEntity.getBody().getMessage());
    }
}