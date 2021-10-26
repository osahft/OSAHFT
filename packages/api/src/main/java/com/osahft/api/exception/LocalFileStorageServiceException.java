package com.osahft.api.exception;

import com.osahft.api.model.ErrorResponse;

public class LocalFileStorageServiceException extends OSAHFTApiException {
    public LocalFileStorageServiceException(ErrorResponse message) {
        super(message);
    }

    public LocalFileStorageServiceException(ErrorResponse message, Exception e) {
        super(message, e);
    }

    public LocalFileStorageServiceException(String message, Exception e) {
        super(message, e);
    }
}
