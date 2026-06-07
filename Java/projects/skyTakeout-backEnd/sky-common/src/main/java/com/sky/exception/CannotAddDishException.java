package com.sky.exception;

public class CannotAddDishException extends BaseException {

    public CannotAddDishException() {
    }
    public CannotAddDishException(String message) {
        super(message);
    }
}
