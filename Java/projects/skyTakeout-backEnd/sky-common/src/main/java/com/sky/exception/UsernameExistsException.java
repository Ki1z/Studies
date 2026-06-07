package com.sky.exception;

public class UsernameExistsException extends BaseException {

    public UsernameExistsException() {}

    public UsernameExistsException(String message) {
        super(message);
    }
}
