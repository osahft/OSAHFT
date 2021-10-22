package com.osahft.api.exception;

public class FileServiceClientException extends OSAHFTApiException {
    public FileServiceClientException(String message, Exception e) {
        super(message, e);
    }
}
