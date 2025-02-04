package com.finapp.backend.exception;

public class DocumentUploadException extends RuntimeException {
    public DocumentUploadException(String message){
        super(message);
    }
    public DocumentUploadException(String message, Throwable throwable) {
    }
}
