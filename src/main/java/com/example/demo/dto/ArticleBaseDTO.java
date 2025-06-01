package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ArticleBaseDTO (
        Long id,
        String title,
        LocalDateTime createTime,
        LocalDateTime updateTime
){
}
