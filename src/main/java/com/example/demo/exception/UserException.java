package com.example.demo.exception;


import com.example.demo.exception.errors.ErrorCode;

public class UserException extends BaseException {
    public UserException(ErrorCode errorCode) {
        super(errorCode, "UserError");
    }
}
