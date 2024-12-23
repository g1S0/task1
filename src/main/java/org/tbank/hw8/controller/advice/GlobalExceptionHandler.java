package org.tbank.hw8.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tbank.hw5.dto.ResponseDto;
import org.tbank.hw8.exception.*;
import org.tbank.hw8.exception.model.CustomErrorResponse;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        CustomErrorResponse response = new CustomErrorResponse(message, status.value());
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponseWithHeaders(String message, HttpHeaders headers) {
        CustomErrorResponse response = new CustomErrorResponse(message, HttpStatus.SERVICE_UNAVAILABLE.value());
        return new ResponseEntity<>(response, headers, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ResponseEntity<CustomErrorResponse> handleUnsupportedCurrency(UnsupportedCurrencyException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAuthorizationHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto<String>> handleInvalidAuthorizationHeaderException(InvalidAuthorizationHeaderException ex) {
        ResponseDto<String> response = new ResponseDto<>(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyIsNotSupportedByCbException.class)
    public ResponseEntity<CustomErrorResponse> handleCurrencyNotFound(CurrencyIsNotSupportedByCbException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "3600");
        return buildErrorResponseWithHeaders(ex.getMessage(), headers);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        String firstErrorMessage = errors.values().stream().findFirst().orElse("Validation error");
        CustomErrorResponse response = new CustomErrorResponse(firstErrorMessage, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {
        return buildErrorResponse("Unknown error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidConfirmationCodeException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidConfirmationCodeException(InvalidConfirmationCodeException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
}