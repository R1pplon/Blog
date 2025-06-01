package com.example.demo.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.example.demo.model.Article}
 */
public record UpdateArticleRequest(String title, String content) implements Serializable {
}