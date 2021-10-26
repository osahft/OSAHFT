package com.osahft.api.controller;

import com.osahft.api.exception.OSAHFTApiException;
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
        return handleExceptionInternal(ex, ex.getError(),
                new HttpHeaders(), ex.getError().getHttpStatus(), request);
    }
}
