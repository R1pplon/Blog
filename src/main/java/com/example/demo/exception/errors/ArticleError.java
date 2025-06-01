package com.example.demo.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ArticleError implements ErrorCode {
    // TODO  添加文章异常
//    异常描述	    错误码	触发场景

    // 文章不存在
     ARTICLE_NOT_EXIST(1001, "文章不存在", HttpStatus.NOT_FOUND),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
