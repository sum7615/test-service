package com.spxam.test_service.exception;

public class NoTestFoundException extends RuntimeException{
    String msg;
    public NoTestFoundException(String message){
        super(message);
        this.msg=message;
    }
}
