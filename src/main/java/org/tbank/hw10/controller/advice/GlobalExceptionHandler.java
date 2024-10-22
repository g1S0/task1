package org.tbank.hw10.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.tbank.hw10.exception.EntityNotFoundException;
import org.tbank.hw10.exception.RelatedEntityNotFoundException;
import org.tbank.hw5.exception.model.CustomErrorResponse;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        CustomErrorResponse response = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RelatedEntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleRelatedEntityNotFound(RelatedEntityNotFoundException ex) {
        CustomErrorResponse response = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGeneralExceptions(Exception ex) {
        CustomErrorResponse response = new CustomErrorResponse("An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        String firstErrorMessage = errors.values().stream().findFirst().orElse("Validation error");
        CustomErrorResponse response = new CustomErrorResponse(firstErrorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
