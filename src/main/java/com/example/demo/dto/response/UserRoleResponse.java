package com.example.demo.dto.response;

import jakarta.annotation.Nullable;
import lombok.Builder;


@Builder
public record UserRoleResponse(
        Integer  role
) {
}
