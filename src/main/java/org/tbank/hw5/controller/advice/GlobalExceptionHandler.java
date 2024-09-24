package org.tbank.hw5.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.tbank.hw5.exceptions.EntityAlreadyExistsException;
import org.tbank.hw5.exceptions.model.CustomErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(customErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse("Произошла ошибка: " + ex.getMessage());
        return new ResponseEntity<>(customErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
