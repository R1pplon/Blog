package com.example.demo.service.impl;

import com.example.demo.dto.ArticleBaseDTO;
import com.example.demo.dto.response.ArticleListResponse;
import com.example.demo.dto.response.ArticleResponse;
import com.example.demo.exception.ArticleException;
import com.example.demo.exception.errors.ArticleError;
import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public ArticleResponse getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ArticleError.ARTICLE_NOT_EXIST));

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .createTime(article.getCreateTime())
                .updateTime(article.getUpdateTime())
                .build();
    }

    @Override
    public ArticleListResponse getArticleList(Integer page, Integer size) {

        Page<Article> articlePage = articleRepository.findAllByOrderByCreateTimeDesc(
                PageRequest.of(page, size)
        );

        List<ArticleBaseDTO> articleDTOs = articlePage.getContent().stream()
                .map(article -> new ArticleBaseDTO(
                        article.getId(),
                        article.getTitle(),
                        article.getCreateTime(),
                        article.getUpdateTime()
                ))
                .toList();

        return new ArticleListResponse(articleDTOs);
    }
}
