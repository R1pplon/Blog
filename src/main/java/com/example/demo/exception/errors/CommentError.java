package com.example.demo.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommentError implements ErrorCode {
//    异常描述	    错误码	触发场景
    // 评论不存在
     COMMENT_NOT_EXIST(1001, "评论不存在", HttpStatus.NOT_FOUND),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
