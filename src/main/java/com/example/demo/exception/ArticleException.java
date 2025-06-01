package com.example.demo.exception;


import com.example.demo.exception.errors.ErrorCode;

public class ArticleException extends BaseException {
    public ArticleException(ErrorCode errorCode) {
        super(errorCode, "TaskError");
    }
}
