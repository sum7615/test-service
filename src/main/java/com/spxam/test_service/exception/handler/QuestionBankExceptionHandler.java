package com.spxam.test_service.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spxam.test_service.exception.ExceptionResponse;
import com.spxam.test_service.exception.InvalidRequestData;

@RestControllerAdvice
public class QuestionBankExceptionHandler {

	@ExceptionHandler(InvalidRequestData.class)
	public ResponseEntity<ExceptionResponse> handleInvalidRequestData(InvalidRequestData ex) {
		ExceptionResponse res = new ExceptionResponse("400", ex.getMessage());
		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
	}
}
