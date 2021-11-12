package com.osahft.api.exception;

import com.osahft.api.model.ErrorResponse;
import lombok.Getter;


@Getter
public class OSAHFTApiException extends Exception {

    protected ErrorResponse error;

    public OSAHFTApiException(ErrorResponse error) {
        super(error.getMessage());
        this.error = error;
    }

    public OSAHFTApiException(ErrorResponse error, Exception e) {
        super(error.getMessage(), e);
        this.error = error;
    }

    /*
    use the constructors below only if the exception could not be caused directly by an api call
     */
    public OSAHFTApiException(String message, Exception e) {
        super(message, e);
    }

    public OSAHFTApiException(String message) {
        super(message);
    }

}
