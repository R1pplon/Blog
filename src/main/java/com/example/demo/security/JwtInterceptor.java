package com.example.demo.security;

import com.example.demo.exception.UserException;
import com.example.demo.exception.errors.UserError;
import com.example.demo.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> EXCLUDE_PATH =
            new ArrayList<>(Arrays.asList(
                    "/api/public/**",
                    "/error",
                    "/v3/api-docs/**",
                    "/swagger-ui*",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/favicon.ico"
            ));

    private final List<String> ADMIN_PATH = Arrays.asList(
            "/api/admin/**"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        String requestURI = request.getRequestURI();

        if (EXCLUDE_PATH.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI))) {
            return true;
        }
        if (cookies == null) {
            throw new UserException(UserError.NO_COOKIE);
        }

//        List<Claims> res = new ArrayList<>();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("jwt_token")) {
//                String token = cookie.getValue();
//                Claims claim = jwtUtils.parseToken(token);
//                res.add(claim);
//            }
//        }
//        if (!res.isEmpty()) {
//            Long userId = res.getFirst().get("userId", Long.class);
//            request.setAttribute("userId", userId);
//            return true;
//        } else throw new UserException(UserError.NO_COOKIE);
        Claims claim = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt_token")) {
                String token = cookie.getValue();
                claim = jwtUtils.parseToken(token);
                break;
            }
        }
        if (claim == null) {
            throw new UserException(UserError.NO_COOKIE);
        }

        // 获取用户角色
        Integer role = claim.get("role", Integer.class);
        Long userId = claim.get("userId", Long.class);
        request.setAttribute("userId", userId);

        // 管理员路径检查
        if (ADMIN_PATH.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI))) {
            if (role != 0) {
                throw new UserException(UserError.PERMISSION_DENIED);
            }
        }

        return true;
    }
}
