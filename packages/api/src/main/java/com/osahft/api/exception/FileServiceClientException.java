package com.osahft.api.exception;

public class FileServiceClientException extends Exception {
    public FileServiceClientException(String message, Exception e) {
        super(message, e);
    }
}
