package com.example.demo.dto.response;

import com.example.demo.dto.ArticleBaseDTO;

import java.util.List;

public record ArticleListResponse (
        Integer total,
        List<ArticleBaseDTO> articles
){
}
