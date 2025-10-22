package com.spxam.test_service.exception;

public class InvalidRequestData extends RuntimeException{
    public InvalidRequestData(String message){
        super(message);
    }
}
