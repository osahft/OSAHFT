package com.osahft.api.controller;

import com.osahft.api.constant.ErrorConstants;
import com.osahft.api.exception.OSAHFTApiException;
import com.osahft.api.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {OSAHFTApiException.class})
    protected ResponseEntity<Object> handleConflict(OSAHFTApiException ex, WebRequest request) {
//        ErrorResponse errorResponseBody = new ErrorResponse(ex.getError());
        ErrorResponse errorResponseBody = new ErrorResponse(ErrorConstants.NOT_FOUND);

//        return handleExceptionInternal(ex, errorResponseBody,
//                new HttpHeaders(), ex.getError().getHttpStatus(), request);
        return handleExceptionInternal(ex, errorResponseBody,
                new HttpHeaders(), ErrorConstants.NOT_FOUND.getHttpStatus(), request);
    }
}
