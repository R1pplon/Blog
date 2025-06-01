package com.example.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "创建评论请求")
public record CreateCommentRequest (
        @Schema(description = "评论内容",example = "写得真好")
        String content,
        @Schema(description = "文章id",example = "1")
        Long articleId,
        @Schema(description = "父级评论id")
        Long parentId
){
}
