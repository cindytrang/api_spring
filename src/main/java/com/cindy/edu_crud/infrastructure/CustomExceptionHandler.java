package com.cindy.edu_crud.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidDatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleInvalidDatesException(InvalidDatesException e, WebRequest request) {
        CustomExceptionDto customExceptionDto = prepareCustomExceptionDto(e, HttpStatus.BAD_REQUEST, Collections.singletonList(e.getMessage()));
        return new ResponseEntity<>(customExceptionDto, HttpStatus.BAD_REQUEST);
    }

    private CustomExceptionDto prepareCustomExceptionDto(InvalidDatesException e, HttpStatus status, List<String> strings) {
        return CustomExceptionDto.builder()
                .status(status.value())
                .dateTime(LocalDateTime.now())
                .messages(strings)
                .shortMessage(e.getMessage())
                .build();
    }
}
