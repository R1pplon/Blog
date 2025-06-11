package com.example.demo.service;

import com.example.demo.dto.UpdateArticleRequest;
import com.example.demo.dto.request.CreateArticleRequest;
import com.example.demo.dto.response.ArticleListResponse;
import com.example.demo.dto.response.ArticleResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    //  获取文章详情
    ArticleResponse getArticleById(Long articleId);

    //  获取文章列表
    ArticleListResponse getArticleList(Integer page, Integer size);

    void createArticle(@Valid CreateArticleRequest createArticleRequest, Long userId);

    void deleteArticleById(Long articleId);

    void updateArticle(@Valid UpdateArticleRequest updateArticleRequest, Long articleId);

    String storeFile(Long userId, MultipartFile file);
}
