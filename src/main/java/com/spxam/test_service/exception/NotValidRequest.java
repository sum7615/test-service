package com.spxam.test_service.exception;

public class NotValidRequest extends RuntimeException{
     private static final long serialVersionUID = 2488506983175102978L;
	String str;
    public NotValidRequest(String message) {
        super(message);
        this.str=message;
    }
}
