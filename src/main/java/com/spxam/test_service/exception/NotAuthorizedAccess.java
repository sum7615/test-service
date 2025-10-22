package com.spxam.test_service.exception;

public class NotAuthorizedAccess extends RuntimeException{
    public NotAuthorizedAccess(String message){
        super(message);
    }
}
