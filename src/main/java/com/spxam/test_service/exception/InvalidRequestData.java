package com.spxam.test_service.exception;

public class InvalidRequestData extends RuntimeException{
    private static final long serialVersionUID = -1329713669434672831L;

	public InvalidRequestData(String message){
        super(message);
    }
}
