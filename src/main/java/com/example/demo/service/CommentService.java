package com.example.demo.service;

import com.example.demo.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    //  获取文章下的所有评论
    List<CommentDTO> getCommentsByArticleId(Long articleId);
}
