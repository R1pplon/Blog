package com.example.demo.dto;

import java.time.LocalDateTime;

public record ArticleBaseDTO (
        Long id,
        String title,
        LocalDateTime createTime,
        LocalDateTime updateTime
){
}
