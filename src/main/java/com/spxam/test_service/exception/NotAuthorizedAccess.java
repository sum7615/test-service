package com.spxam.test_service.exception;

public class NotAuthorizedAccess extends RuntimeException{
    private static final long serialVersionUID = -8807223521378955657L;

	public NotAuthorizedAccess(String message){
        super(message);
    }
}
