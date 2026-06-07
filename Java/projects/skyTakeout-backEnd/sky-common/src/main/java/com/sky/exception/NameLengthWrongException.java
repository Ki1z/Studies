package com.sky.exception;

public class NameLengthWrongException extends RuntimeException {

    public NameLengthWrongException() {
    }
    public NameLengthWrongException(String message) {
        super(message);
    }
}
