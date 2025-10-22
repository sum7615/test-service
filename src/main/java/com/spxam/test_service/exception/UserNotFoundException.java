package com.spxam.test_service.exception;

public class UserNotFoundException extends RuntimeException{
    private static final long serialVersionUID = -5564323859302216870L;

	public UserNotFoundException(String message){
        super(message);
    }
}
