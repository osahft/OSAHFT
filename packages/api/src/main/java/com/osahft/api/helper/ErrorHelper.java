package com.osahft.api.helper;

import com.osahft.api.model.ErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;


@ToString
public class ErrorHelper {

    private static final ErrorResponse BAD_REQUEST = new ErrorResponse("-00001", "", HttpStatus.BAD_REQUEST);
    private static final ErrorResponse TOO_MANY_REQUESTS = new ErrorResponse("-00002", "",
            HttpStatus.TOO_MANY_REQUESTS);
    @Getter
    private static final ErrorResponse UNAUTHORIZED = new ErrorResponse("-10001",
            "The sender email is not authorized. Use POST /api/v1/transfers/mails/{mail_transfer_id}/auth/{authentication_code} first.",
            HttpStatus.UNAUTHORIZED);
    private static final ErrorResponse FORBIDDEN = new ErrorResponse("-10002", "", HttpStatus.FORBIDDEN);
    private static final ErrorResponse NOT_FOUND = new ErrorResponse("-20001", "MailTransfer not found: %s.",
            HttpStatus.NOT_FOUND);
    private static final ErrorResponse SERVICE_UNAVAILABLE = new ErrorResponse("-30001", "",
            HttpStatus.SERVICE_UNAVAILABLE);
    @Getter
    private static final ErrorResponse INTERNAL_SERVER_ERROR = new ErrorResponse("-30002",
            "An internal server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);

    public static ErrorResponse getBAD_REQUEST(String message) {
        return new ErrorResponse(BAD_REQUEST.getCode(), message, BAD_REQUEST.getHttpStatus());
    }

    public static ErrorResponse getNOT_FOUND(String mailTransferId) {
        return new ErrorResponse(NOT_FOUND.getCode(), String.format(NOT_FOUND.getMessage(), mailTransferId),
                NOT_FOUND.getHttpStatus());
    }

    public static ErrorResponse getSERVICE_UNAVAILABLE(String message) {
        return new ErrorResponse(SERVICE_UNAVAILABLE.getCode(), message, SERVICE_UNAVAILABLE.getHttpStatus());
    }

    public static ErrorResponse getTOO_MANY_REQUESTS(String message) {
        return new ErrorResponse(TOO_MANY_REQUESTS.getCode(), message, TOO_MANY_REQUESTS.getHttpStatus());
    }

    public static ErrorResponse getFORBIDDEN(String message) {
        return new ErrorResponse(FORBIDDEN.getCode(), message, FORBIDDEN.getHttpStatus());
    }

}
