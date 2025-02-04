package com.finapp.backend.exception;

public class CustomFinAppException extends RuntimeException {
    public CustomFinAppException(String message){
        super(message);
    }
    public CustomFinAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
