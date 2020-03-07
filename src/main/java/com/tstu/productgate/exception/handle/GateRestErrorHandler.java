package com.tstu.productgate.exception.handle;

import com.tstu.commons.dto.http.response.error.ErrorField;
import com.tstu.commons.dto.http.response.error.ErrorResponse;
import com.tstu.commons.dto.http.response.error.ErrorValidationResponse;
import com.tstu.commons.exception.PrsHttpException;
import com.tstu.commons.exception.handle.PrsErrorHandler;
import com.tstu.productgate.exception.ProductGateErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GateRestErrorHandler extends PrsErrorHandler {


    @Override
    @ExceptionHandler({PrsHttpException.class})
    protected ResponseEntity<Object> prsHttpExceptionHandler(PrsHttpException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder().id(UUID.randomUUID().toString()).time(LocalDateTime.now()).code(ex.getErrorCode().toString()).message(ex.getMessage()).displayMessage(ex.getMessage()).techInfo((Object)null).build();
        return this.handleExceptionInternal(ex, errorResponse, new HttpHeaders(), ex.getHttpStatus(), request);
    }


    @ExceptionHandler({ConnectException.class})
    protected ResponseEntity<Object> connectExceptionHandler(ConnectException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .time(LocalDateTime.now())
                .code(ProductGateErrors.CONNECTION_ERROR.toString())
                .message(ex.getMessage())
                .displayMessage(ex.getMessage())
                .techInfo((Object)null).build();

        return this.handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .time(LocalDateTime.now())
                .code(ProductGateErrors.JSON_NOT_READABLE.name())
                .message(ex.getMessage())
                .displayMessage(ProductGateErrors.JSON_NOT_READABLE.getErrorDescription())
                .techInfo(null)
                .build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldError = ex.getBindingResult().getFieldErrors();
        List<ErrorField> errorFields = (List)fieldError.stream().map((fe) -> new ErrorField(fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());
        ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(errorFields);
        return this.handleExceptionInternal(ex, errorValidationResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}