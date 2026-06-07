package com.sky.exception;

public class NoSuchDishException extends BaseException {

    public NoSuchDishException() {
    }
    public NoSuchDishException(String message) {
        super(message);
    }
}
