package com.example.demo.dto.request;

import java.io.Serializable;

/**
 * DTO for {@link com.example.demo.model.Article}
 */
public record CreateArticleRequest(
        String title, String content
) implements Serializable {
  }