package com.osahft.api.helper;

import com.osahft.api.model.ErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
public class ErrorHelper {


    private static final ErrorResponse BAD_REQUEST = new ErrorResponse("-00001", "", HttpStatus.BAD_REQUEST);
    @Getter
    private static final ErrorResponse UNAUTHORIZED = new ErrorResponse("-10001", "The sender email is not authorized. Use POST /api/v1/transfers/mails/{mail_transfer_id}/auth/{authentication_code} first.", HttpStatus.UNAUTHORIZED);
    private static final ErrorResponse NOT_FOUND = new ErrorResponse("-20001", "MailTransfer not found: %s.", HttpStatus.NOT_FOUND);
    private static final ErrorResponse SERVICE_UNAVAILABLE = new ErrorResponse("-30001", "", HttpStatus.SERVICE_UNAVAILABLE);

    public static ErrorResponse getBAD_REQUEST(String message) {
        return new ErrorResponse(BAD_REQUEST.getCode(), message, BAD_REQUEST.getHttpStatus());
    }

    public static ErrorResponse getNOT_FOUND(String mailTransferId) {
        return new ErrorResponse(NOT_FOUND.getCode(), String.format(NOT_FOUND.getMessage(), mailTransferId), NOT_FOUND.getHttpStatus());
    }

    public static ErrorResponse getSERVICE_UNAVAILABLE(String message) {
        return new ErrorResponse(SERVICE_UNAVAILABLE.getCode(), message, SERVICE_UNAVAILABLE.getHttpStatus());
    }
}
