package com.example.demo.repository;

import com.example.demo.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    // 按创建时间倒序分页查询
    Page<Article> findAllByOrderByCreateTimeDesc(Pageable pageable);

    Article findByTitle(String title);
}
