package com.spxam.test_service.exception.handler;

import com.spxam.test_service.exception.ExceptionResponse;
import com.spxam.test_service.exception.NoTestFoundException;
import com.spxam.test_service.exception.NotValidRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TestDataExceptionHandler {

    @ExceptionHandler(NoTestFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoTestFoundException(NoTestFoundException ex) {
        ExceptionResponse response = new ExceptionResponse("404", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidRequest.class)
    public ResponseEntity<ExceptionResponse> handle(NotValidRequest ex) {
        ExceptionResponse response = new ExceptionResponse("400", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException ex) {
        ExceptionResponse response = new ExceptionResponse("500", "Internal Server Error: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
