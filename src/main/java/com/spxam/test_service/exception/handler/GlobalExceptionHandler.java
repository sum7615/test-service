package com.spxam.test_service.exception.handler;

import com.spxam.test_service.exception.ExceptionResponse;
import com.spxam.test_service.exception.InternalServerError;
import com.spxam.test_service.exception.NotAuthorizedAccess;
import com.spxam.test_service.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(UserNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse("404", ex.getMessage());
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedAccess.class)
    public ResponseEntity<ExceptionResponse> handle(NotAuthorizedAccess ex) {
        ExceptionResponse response = new ExceptionResponse("403", ex.getMessage());
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ExceptionResponse> handle(InternalServerError ex) {
        ExceptionResponse response = new ExceptionResponse("403", ex.getMessage());
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
