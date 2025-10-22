package com.spxam.test_service.exception;

public class NotValidRequest extends RuntimeException{
     String str;
    public NotValidRequest(String message) {
        super(message);
        this.str=message;
    }
}
