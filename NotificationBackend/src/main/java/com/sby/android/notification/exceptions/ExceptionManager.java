package com.sby.android.notification.exceptions;

import com.sby.android.notification.dto.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpError> handle(MethodArgumentNotValidException ex) {
        var reasons = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        var httpError = new HttpError(400,
                "Bad Request",
                reasons
        );
        return new ResponseEntity<>(httpError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpError> handle(NotFoundException ex) {
        return new ResponseEntity<>(new HttpError(400, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<HttpError> handle(CustomHttpException ex) {
        return new ResponseEntity<>(new HttpError(ex.getStatusCode(), ex.getMessage()), HttpStatus.valueOf(ex.getStatusCode()));
    }

}
