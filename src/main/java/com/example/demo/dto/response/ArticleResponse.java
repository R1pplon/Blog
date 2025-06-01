package com.example.demo.dto.response;

import com.example.demo.model.Comment;
import com.example.demo.model.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ArticleResponse (
        Long id,
        String title,
        String content,
        LocalDateTime createTime,
        LocalDateTime updateTime
){
}
