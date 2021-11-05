package com.osahft.api.exception;

import com.osahft.api.model.ErrorResponse;

public class RateLimitInterceptorException extends OSAHFTApiException {
    public RateLimitInterceptorException(ErrorResponse message) {
        super(message);
    }
}
