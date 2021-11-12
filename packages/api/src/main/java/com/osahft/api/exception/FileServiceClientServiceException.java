package com.osahft.api.exception;

import com.osahft.api.model.ErrorResponse;

public class FileServiceClientServiceException extends OSAHFTApiException {
    public FileServiceClientServiceException(String message, Exception e) {
        super(message, e);
    }

    public FileServiceClientServiceException(ErrorResponse message, Exception e) {
        super(message, e);
    }
}
