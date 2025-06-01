package com.example.demo.service.impl;

import com.example.demo.dto.CommentDTO;
import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
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
                            comment.getContent(),
                            comment.getCreateTime(),
                            parentId
                    );
                })
                .collect(Collectors.toList());
    }
}
