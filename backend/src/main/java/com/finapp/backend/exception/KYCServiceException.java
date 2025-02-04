package com.finapp.backend.exception;

public class KYCServiceException extends RuntimeException{

    public KYCServiceException(String message){
        super(message);
    }
    public KYCServiceException(String message, Throwable cause){
        super(message, cause);
    }
}
