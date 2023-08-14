package org.originit.demo.exception;

import org.originit.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message, Integer code) {
        super(message, code);
    }
}
