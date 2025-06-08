package com.example.demo.dto.response;

import jakarta.annotation.Nullable;
import lombok.Builder;


@Builder
public record UserResponse(
        Long id,
        String username,
        String email,
        @Nullable String avatarUrl
) {
}
