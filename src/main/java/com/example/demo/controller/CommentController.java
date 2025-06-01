package com.example.demo.controller;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.request.CreateCommentRequest;
import com.example.demo.service.CommentService;
import com.example.demo.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评论相关Api", description = "用于评论")
@RestController
@RequestMapping("/api/comment/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "发表评论", description = "发表评论")
    @PostMapping()
    public ApiResponse<String> createComment(@RequestBody CreateCommentRequest request,
                                             @RequestAttribute("userId") Long userId){
        commentService.createComment(request, userId);
        return ApiResponse.success("评论成功");
    }

    @Operation(summary = "获取评论", description = "获取当前用户的所有评论")
    @GetMapping()
    public ApiResponse<List<CommentDTO>> getComment(@RequestAttribute("userId") Long userId){
        return ApiResponse.success(commentService.getAllCommentByUserId(userId));
    }

    @Operation(summary = "删除评论", description = "删除指定的评论")
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> updateComment(@PathVariable Long commentId){
         commentService.deleteComment(commentId);
         return ApiResponse.success("删除成功");
    }
}
