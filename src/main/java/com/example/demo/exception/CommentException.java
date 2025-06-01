package com.example.demo.exception;

import com.example.demo.exception.errors.ErrorCode;

public class CommentException extends BaseException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode, "ListError");
    }
}
