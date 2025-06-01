package com.example.demo.service;

import com.example.demo.dto.response.ArticleListResponse;
import com.example.demo.dto.response.ArticleResponse;

public interface ArticleService {

    //  获取文章详情
    ArticleResponse getArticleById(Long articleId);

    //  获取文章列表
    ArticleListResponse getArticleList(Integer page, Integer size);
}
