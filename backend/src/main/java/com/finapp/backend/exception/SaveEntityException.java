package com.finapp.backend.exception;

public class SaveEntityException extends RuntimeException {

    public SaveEntityException(String message, Throwable cause){
        super(message, cause);
    }
}
