package com.finapp.backend.exception;

public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(String message){
        super(message);
    }
}
