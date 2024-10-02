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

    private static final String ENTITY_ALREADY_EXISTS_MESSAGE = "Entity already exists";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found";
    private static final String GENERIC_ERROR_MESSAGE = "Some generic error";

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    private void assertErrorResponse(ResponseEntity<CustomErrorResponse> responseEntity, HttpStatus expectedStatus, String expectedMessage) {
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedMessage, responseEntity.getBody().getMessage());
    }

    @Test
    public void testHandleEntityAlreadyExistsException() {
        EntityAlreadyExistsException exception = new EntityAlreadyExistsException(ENTITY_ALREADY_EXISTS_MESSAGE);
        ResponseEntity<CustomErrorResponse> responseEntity = exceptionHandler.handleEntityAlreadyExistsException(exception);

        assertErrorResponse(responseEntity, HttpStatus.CONFLICT, ENTITY_ALREADY_EXISTS_MESSAGE);
    }

    @Test
    public void testHandleEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE);
        ResponseEntity<CustomErrorResponse> responseEntity = exceptionHandler.handleEntityNotFoundException(exception);

        assertErrorResponse(responseEntity, HttpStatus.CONFLICT, ENTITY_NOT_FOUND_MESSAGE);
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new Exception(GENERIC_ERROR_MESSAGE);
        ResponseEntity<CustomErrorResponse> responseEntity = exceptionHandler.handleGenericException(exception);

        assertErrorResponse(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_ERROR_MESSAGE);
    }
}