package com.example.demo.controller;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.ArticleListResponse;
import com.example.demo.dto.response.ArticleResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.CookieUtil;
import com.example.demo.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "游客相关Api", description = "公共接口")
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
    private final UserService userService;
    private final ArticleService articeleService;
    private final CommentService commentService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ArticleService articleService;

    // ================= 公共接口 ================= //
    /**
     * 用户注册
     */
    @Operation(summary = "注册", description = "输入用户名密码和邮箱，进行注册")
    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ApiResponse.success("注册成功");
    }

    /**
     * 用户登录
     * 
     * @param request
     * @param response
     * @return
     */
    @Operation(summary = "登录", description = "传入用户名和密码, 如果登陆成功就返回一个 cookie 给前端. 这个 cookie 的值就是 jwt_toke.")
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        UserResponse userResponse = userService.login(request);
        User user = userRepository.findByUsername(request.username());
        String token = jwtUtils.generateToken(user.getId(), user.getRole());
        CookieUtil.setCookie(response, token);

        return ApiResponse.success(userResponse);
    }

    /**
     * 获取文章详情
     */
    @Operation(summary = "获取文章详情", description = "传入文章 id, 返回文章详情")
    @GetMapping("/article/{ArticleId}")
    public ApiResponse<ArticleResponse> getArticleDetail(@PathVariable Long ArticleId) {
        ArticleResponse articleResponse = articeleService.getArticleById(ArticleId);
        return ApiResponse.success(articleResponse);
    }

    /**
     * 获取文章列表
     */
    @Operation(summary = "获取文章列表", description = "传入页码和页面大小, 返回文章列表")
    @GetMapping("/article")
    public ApiResponse<ArticleListResponse> getArticleList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        page = page - 1;

        ArticleListResponse response = articleService.getArticleList(page, size);
        return ApiResponse.success(response);
    }

    /**
     * 根据文章id 获取评论列表
     */
    @Operation(summary = "获取评论列表", description = "传入文章id, 返回评论列表")
    @GetMapping("/article/{articleId}/comments")
    public ApiResponse<List<CommentDTO>> getCommentsByArticleId(@PathVariable Long articleId) {
        List<CommentDTO> comments = commentService.getCommentsByArticleId(articleId);
        return ApiResponse.success(comments);
    }
}
