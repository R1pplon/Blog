package com.example.demo.dto;

public record PaginationDTO(
        int page,           // 当前页码
        int size,           // 每页条数
        int totalPages,     // 总页数
        long totalItems     // 总记录数
) {}