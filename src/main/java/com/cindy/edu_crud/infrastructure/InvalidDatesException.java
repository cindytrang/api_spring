package com.cindy.edu_crud.infrastructure;

public class InvalidDatesException extends RuntimeException {
    public InvalidDatesException(String message) {
        super(message);
    }
}
