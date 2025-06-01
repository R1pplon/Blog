package com.example.demo.exception;

import com.example.demo.exception.errors.ErrorCode;
import lombok.Getter;
import lombok.Setter;

//@RequiredArgsConstructor
@Getter
@Setter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String errorType;

    public BaseException( ErrorCode errorCode, String errorType) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorType = errorType;
    }
}
