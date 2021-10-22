package com.osahft.api.exception;

public class LocalFileStorageServiceException extends OSAHFTApiException {
    public LocalFileStorageServiceException(String message) {
        super(message);
    }

    public LocalFileStorageServiceException(String message, Exception e) {
        super(message, e);
    }
}
