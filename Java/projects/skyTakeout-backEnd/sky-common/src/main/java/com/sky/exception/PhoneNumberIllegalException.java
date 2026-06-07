package com.sky.exception;

/**
 * 手机号非法异常
 */
public class PhoneNumberIllegalException extends BaseException {

    public PhoneNumberIllegalException() {}

    public PhoneNumberIllegalException(String message) {
        super(message);
    }
}
