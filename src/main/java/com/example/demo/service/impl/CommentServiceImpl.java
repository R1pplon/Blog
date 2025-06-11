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
     * @param articleId 文章ID，用于查询评论
     * @return 返回评论的DTO列表，CommentDTO是Comment实体的传输层对象
     */
    @Override
    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        // 查询所有属于特定文章的评论
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
        // 将查询到的评论列表转换为DTO格式的列表
        return getCommentDTOS(comments);
    }

    /**
     * 创建评论
     * 此方法用于处理用户对文章的评论创建操作，包括对用户和文章的存在性校验，
     * 以及评论的初始化和保存
     *
     * @param createCommentRequest 包含评论相关信息的请求对象，如文章ID和评论内容
     * @param userId               发表评论的用户ID
     */
    @Override
    public void createComment(CreateCommentRequest createCommentRequest , Long userId) {
        // 根据用户ID获取用户信息，如果用户不存在，则抛出异常
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));

        // 根据文章ID获取文章信息，如果文章不存在，则抛出异常
        Article article = articleRepository.findById(createCommentRequest.articleId()).orElseThrow(() -> new ArticleException(ArticleError.ARTICLE_NOT_EXIST));

        // 初始化评论对象
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(createCommentRequest.content());
        comment.setCreateTime(LocalDateTime.now());
        comment.setStatus(1);
        comment.setArticle(article);

        // 如果评论有父评论ID，则获取父评论信息并设置
        if (createCommentRequest.parentId() != null){
            Comment parentComment = commentRepository.findById(createCommentRequest.parentId()).orElseThrow(() -> new CommentException(CommentError.COMMENT_NOT_EXIST));
            comment.setParent(parentComment);
        }

        // 保存评论到数据库
        commentRepository.save(comment);
    }

    /**
     * 获取用户所有评论
     *
     * @param userId 用户ID，用于查询该用户的所有评论
     * @return 返回一个包含用户所有评论的List<CommentDTO>对象
     */
    @Override
    public List<CommentDTO> getAllCommentByUserId(Long userId) {
        // 根据用户ID查询评论实体列表
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        // 将查询到的评论实体列表转换为DTO列表并返回
        return getCommentDTOS(comments);
    }

    /**
     * 删除指定的评论
     * 此方法首先尝试在数据库中查找指定ID的评论如果评论不存在，将抛出异常
     * 一旦找到评论，将调用delete方法将其从数据库中移除
     *
     * @param commentId 要删除的评论的ID，必须是数据库中已存在的ID
     * @throws CommentException 如果指定ID的评论不存在，抛出此异常
     */
    @Override
    public void deleteComment(Long commentId) {
        // 通过ID查找评论，如果不存在则抛出异常
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentError.COMMENT_NOT_EXIST));
        // 删除找到的评论
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
                            comment.getArticle().getId(),
                            comment.getContent(),
                            comment.getCreateTime(),
                            parentId
                    );
                })
                .collect(Collectors.toList());
    }


}
