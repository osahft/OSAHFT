package com.osahft.api.controller;

import java.util.stream.Collectors;

import com.osahft.api.exception.OSAHFTApiException;
import com.osahft.api.helper.ErrorHelper;
import com.osahft.api.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
public class ErrorController extends ResponseEntityExceptionHandler {

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorResponse response) {
        return new ResponseEntity<>(response, new HttpHeaders(), response.getHttpStatus());
    }

    @ExceptionHandler(value = OSAHFTApiException.class)
    protected ResponseEntity<ErrorResponse> handleOSAHFTApiException(OSAHFTApiException ex, WebRequest request) {
        log.error("Received request " + request + " that caused the exception " + ex);
        return createErrorResponseEntity(ex.getError());
    }

    @ExceptionHandler(value = MultipartException.class)
    protected ResponseEntity<ErrorResponse> handleSizeLimitExceeded(SizeLimitExceededException ex, WebRequest request) {
        log.error("Received request which exceeded file or request size. The request " + request +
                " caused the exception " + ex);
        return createErrorResponseEntity(ErrorHelper.getBAD_REQUEST("File size exceeded"));
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex, WebRequest request) {
        log.error("Received request that caused an internal server error. The request " + request +
                " caused the exception " + ex);
        return createErrorResponseEntity(ErrorHelper.getINTERNAL_SERVER_ERROR());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
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
