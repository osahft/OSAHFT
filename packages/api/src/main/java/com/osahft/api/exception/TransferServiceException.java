package com.osahft.api.exception;

import com.osahft.api.model.ErrorResponse;

public class TransferServiceException extends OSAHFTApiException {
    public TransferServiceException(ErrorResponse message) {
        super(message);
    }
}
