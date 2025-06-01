package com.example.demo.repository;

import com.example.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 根据 文章id查询所有评论
    List<Comment> findAllByArticleId(Long articleId);

    //  根据 用户id查询所有评论
    List<Comment> findAllByUserId(Long userId);

    List<Comment> findAllByStatus(int i);
}
