package com.osahft.api.exception;

public class OSAHFTApiException extends Exception {
    public OSAHFTApiException(String message) {
        super(message);
    }

    public OSAHFTApiException(String message, Exception e) {
        super(message, e);
    }

}
