package com.infyme.digitalbanking.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class InfyMeDigitalBankingGlobalExceptionHandler {

    private final MessageSource messageSource;

    public InfyMeDigitalBankingGlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InfyMeDigitalBankingException.class)
    public ResponseEntity<ErrorInformation> handleBusinessException(InfyMeDigitalBankingException ex) {
        ErrorInformation error = new ErrorInformation();
        error.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        error.setErrorMessage(ex.getMessage());
        error.setErrorTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInformation> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorInformation error = new ErrorInformation();
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(validationError -> validationError instanceof FieldError fieldError
                        ? fieldError.getField() + ": " + validationError.getDefaultMessage()
                        : validationError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        error.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        error.setErrorMessage(errorMessage);
        error.setErrorTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorInformation> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorInformation error = new ErrorInformation();
        error.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        error.setErrorMessage(ex.getMessage());
        error.setErrorTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInformation> handleGeneric(Exception ex) {
        ErrorInformation error = new ErrorInformation();
        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        error.setErrorMessage(messageSource.getMessage(ExceptionConstants.SERVER_ERROR.getCode(), null, Locale.getDefault()));
        error.setErrorTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
