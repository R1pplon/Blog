package com.example.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateCommentRequest(
        @Schema(description = "评论id",example = "1")
        Long commentId,
        @Schema(description = "更新的评论内容",example = "更新的评论内容")
        String content
) {
}
