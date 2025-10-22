package com.spxam.test_service.exception;

public class NoTestFoundException extends RuntimeException{
    private static final long serialVersionUID = -6003581697129817430L;
	String msg;
    public NoTestFoundException(String message){
        super(message);
        this.msg=message;
    }
}
