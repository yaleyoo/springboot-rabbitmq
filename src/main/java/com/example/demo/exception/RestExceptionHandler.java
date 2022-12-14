package com.example.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author STEVE on 1/11/2022
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler({ BookNotFoundException.class })
//    protected ResponseEntity<Object> handleNotFound(
//            Exception ex, WebRequest request) {
//        return handleExceptionInternal(ex, "Book not found",
//                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleBadRequest(
            Exception ex, WebRequest request) {
        ex.printStackTrace();
        return handleExceptionInternal(ex, ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
