package com.example.demo.service.impl;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.request.CreateCommentRequest;
import com.example.demo.exception.ArticleException;
import com.example.demo.exception.CommentException;
import com.example.demo.exception.UserException;
import com.example.demo.exception.errors.ArticleError;
import com.example.demo.exception.errors.CommentError;
import com.example.demo.exception.errors.UserError;
import com.example.demo.model.Article;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    /**
     * 根据文章id获取评论列表
     * @param articleId
     * @return
     */
    @Override
    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
        return getCommentDTOS(comments);
    }

    /**
     * 创建评论
     * @param createCommentRequest
     * @param userId
     */
    @Override
    public void createComment(CreateCommentRequest createCommentRequest , Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));
        Article article = articleRepository.findById(createCommentRequest.articleId()).orElseThrow(() -> new ArticleException(ArticleError.ARTICLE_NOT_EXIST));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(createCommentRequest.content());
        comment.setCreateTime(LocalDateTime.now());
        comment.setStatus(1);
        comment.setArticle(article);

        if (createCommentRequest.parentId() != null){
            Comment parentComment = commentRepository.findById(createCommentRequest.parentId()).orElseThrow(() -> new CommentException(CommentError.COMMENT_NOT_EXIST));
            comment.setParent(parentComment);
        }

        commentRepository.save(comment);
    }

    /**
     * 获取用户所有评论
     * @param userId
     * @return
     */
    @Override
    public List<CommentDTO> getAllCommentByUserId(Long userId) {
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        return getCommentDTOS(comments);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentError.COMMENT_NOT_EXIST));
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDTO> getAllComment() {
        List<Comment> comments = commentRepository.findAll();
        return getCommentDTOS(comments);
    }

    @Override
    public List<CommentDTO> getAllCommentByStatus() {
        List<Comment> comments = commentRepository.findAllByStatus(0);
        return getCommentDTOS(comments);
    }

    @Override
    public void updateCommentStatus(Long commentId) {
        commentRepository.findById(commentId).ifPresent(comment -> {
            comment.setStatus(1);
            commentRepository.save(comment);
        });
    }


    /**
     * 获取评论列表
     * @param comments
     * @return
     */
    private List<CommentDTO> getCommentDTOS(List<Comment> comments) {
        return comments.stream()
                .map(comment -> {
                    // 处理用户信息（可能被懒加载代理包装）
                    String username = comment.getUser().getUsername();
                    String avatarUrl = comment.getUser().getAvatarUrl();

                    // 处理父评论ID（可能为null或懒加载代理）
                    Long parentId = null;
                    if (comment.getParent() != null) {
                        // 处理Hibernate懒加载代理
                        if (comment.getParent() instanceof HibernateProxy) {
                            LazyInitializer initializer = ((HibernateProxy) comment.getParent()).getHibernateLazyInitializer();
                            parentId = (Long) initializer.getIdentifier();
                        } else {
                            parentId = comment.getParent().getId();
                        }
                    }

                    return new CommentDTO(
                            comment.getId(),
                            username,
                            avatarUrl,
                            comment.getArticle().getTitle(),
                            comment.getContent(),
                            comment.getCreateTime(),
                            parentId
                    );
                })
                .collect(Collectors.toList());
    }


}
