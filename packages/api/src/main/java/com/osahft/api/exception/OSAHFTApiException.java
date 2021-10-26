package com.osahft.api.exception;

import com.osahft.api.constant.ErrorConstants;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class OSAHFTApiException extends Exception {

    // TODO SET THIS
    @NotNull
    protected ErrorConstants error;

    public OSAHFTApiException(String message) {
        super(message);
    }

    public OSAHFTApiException(String message, Exception e) {
        super(message, e);
    }

}
