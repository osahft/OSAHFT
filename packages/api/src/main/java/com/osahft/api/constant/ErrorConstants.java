package com.osahft.api.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorConstants {
    // TODO ERRORCODE AND ERROR MESSAGE
    // TODO CUSTOM CODE
    // MailTransferRepositoryException only used for mailtransfer not found in repo 410 Gone
    UNAUTHORIZED("asdf", "", HttpStatus.UNAUTHORIZED),
    NOT_FOUND("asdf", "MailTransfer not found", HttpStatus.NOT_FOUND),
    ;


    public final String code;
    public final String message;
    public final HttpStatus httpStatus;

    ErrorConstants(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

//    public String getCode() {
//        return code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public HttpStatus getHttpStatus() {
//        return httpStatus;
//    }
}
