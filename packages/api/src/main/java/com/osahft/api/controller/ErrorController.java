package com.osahft.api.controller;

import com.osahft.api.exception.OSAHFTApiException;
import com.osahft.api.helper.ErrorHelper;
import com.osahft.api.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ErrorController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {OSAHFTApiException.class})
    protected ResponseEntity<Object> handleConflict(OSAHFTApiException ex, WebRequest request) {
        log.error("Received request " + request + " that caused the exception " + ex);
        return handleExceptionInternal(ex, ex.getError(),
                new HttpHeaders(), ex.getError().getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "Invalid argument(s): " +
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + " " + error.getDefaultMessage())
                        .collect(Collectors.joining(", "));
        ErrorResponse response = ErrorHelper.getBAD_REQUEST(errorMessage);
        log.info("Received invalid argument(s) " + errorMessage + " that caused the exception " + ex);
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), response.getHttpStatus(), request);
    }
}
