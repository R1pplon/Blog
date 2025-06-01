package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.request.CreateCommentRequest;

import java.util.List;

public interface CommentService {

    //  获取文章下的所有评论
    List<CommentDTO> getCommentsByArticleId(Long articleId);

    //  创建评论
    void createComment(CreateCommentRequest createCommentRequest, Long userId);

    //  获取当前用户的所有评论
    List<CommentDTO> getAllCommentByUserId(Long userId);

    //  删除评论
    void deleteComment(Long commentId);

    //  获取所有评论
    List<CommentDTO> getAllComment();

    List<CommentDTO> getAllCommentByStatus();

    void updateCommentStatus(Long commentId);
}
