package com.example.demo.service.impl;

import com.example.demo.dto.ArticleBaseDTO;
import com.example.demo.dto.UpdateArticleRequest;
import com.example.demo.dto.request.CreateArticleRequest;
import com.example.demo.dto.response.ArticleListResponse;
import com.example.demo.dto.response.ArticleResponse;
import com.example.demo.exception.ArticleException;
import com.example.demo.exception.errors.ArticleError;
import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {


    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

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

        Integer total = articleRepository.countBy();

        return new ArticleListResponse(total,articleDTOs);
    }

    @Override
    public void createArticle(CreateArticleRequest createArticleRequest, Long userId) {
        Article article = Article.builder()
                .title(createArticleRequest.title())
                .content(createArticleRequest.content())
                .author(userService.getUserById(userId))
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        articleRepository.save(article);
    }

    @Override
    public void deleteArticleById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Override
    public void updateArticle(UpdateArticleRequest updateArticleRequest, Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ArticleError.ARTICLE_NOT_EXIST));
        if (updateArticleRequest.title() != null){
            article.setTitle(updateArticleRequest.title());
        }

        if (updateArticleRequest.content() != null){
            article.setContent(updateArticleRequest.content());
        }

        article.setUpdateTime(LocalDateTime.now());
        articleRepository.save(article);
    }

    @Override
    public String storeFile(Long userId, MultipartFile file) {
        // 读取文件文本，创建文章
        try {
            // 标题title为文件名
//            String title = file.getOriginalFilename().split("\\.")[0];
            String title = FilenameUtils.getBaseName(file.getOriginalFilename());
            // 文件文本为文章内容content
            String content = new String(file.getBytes());
            createArticle(new CreateArticleRequest(title, content), userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
