package com.example.demo.dto;

import java.time.LocalDateTime;

public record CommentDTO (
        Long id,
        String username,
        String avatarUrl,
        String articleTitle,
        String content,
        LocalDateTime createTime,
        Long parentId
){

}
