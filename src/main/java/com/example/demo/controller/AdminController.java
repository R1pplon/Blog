package com.example.demo.controller;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.UpdateArticleRequest;
import com.example.demo.dto.request.CreateArticleRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.UpdateCommentRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import com.example.demo.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理员相关Api", description = "用于管理")
@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
public class AdminController {

    private final CommentService commentService;
    private final ArticleService articleService;
    private final UserService userService;

    // 用户管理

    @Operation(summary = "获取所有用户", description = "获取所有评用户的信息")
    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> getAllUser() {
        return ApiResponse.success(userService.getAllUser());
    }

    @Operation(summary = "获取用户信息", description = "获取指定用户的详细信息")
     @GetMapping("/users/{id}")
     public ApiResponse<User> getUserById(@PathVariable Long id) {
         return ApiResponse.success(userService.getUserById(id));
    }

     @Operation(summary = "更新用户信息", description = "更新指定用户的信息")
     @PutMapping("/users/{id}")
        public ApiResponse<String> updateUser(@PathVariable Long id, @RequestBody LoginRequest user) {
        userService.update(user,id);
        return ApiResponse.success("管理员修改用户信息成功");
     }

      @Operation(summary = "删除用户", description = "删除指定用户")
      @DeleteMapping("/users/{id}")
       public ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ApiResponse.success("管理员删除用户成功");
      }

      // 文章管理
     @Operation(summary = "创建文章", description = "只有管理员才能创建文章")
     @PostMapping("/articles")
      public ApiResponse<String> createArticle(@Valid @RequestBody CreateArticleRequest createArticleRequest,
     @RequestAttribute("userId") Long userId) {
        articleService.createArticle(createArticleRequest,userId);
        return ApiResponse.success("管理员创建文章成功");
     }

      @Operation(summary = "删除文章", description = "只有管理员才能删除文章")
       @DeleteMapping("/article/{articleId}")
      public ApiResponse<String> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticleById(articleId);
        return ApiResponse.success("管理员删除文章成功");
     }

      @Operation(summary = "更新文章", description = "只有管理员才能更新文章")
      @PutMapping("/article/{articleId}")
      public ApiResponse<String> updateArticle(@PathVariable Long articleId, @RequestBody @Valid UpdateArticleRequest updateArticleRequest) {
        articleService.updateArticle(updateArticleRequest, articleId);
        return ApiResponse.success("管理员更新文章成功");
     }

    // 评论管理

    @Operation(summary = "获取所有评论", description = "获取所有评论的详细信息")
     @GetMapping("/comments")
     public ApiResponse<List<CommentDTO>> getAllComment() {
         List<CommentDTO> comments = commentService.getAllComment();
          return ApiResponse.success(comments);
    }

    @Operation(summary = "删除评论", description = "根据CommentId删除评论")
     @PutMapping("/comments/{commentId}")
     public ApiResponse<String> updateComment(@PathVariable Long commentId){
         commentService.deleteComment(commentId);
          return ApiResponse.success("删除成功");
    }

    @Operation(summary = "获取所有待审核评论", description = "获取所有待审核评论的详细信息")
    @GetMapping("/comments/approve")
     public ApiResponse<List<CommentDTO>> getAllCommentByApprove(){
         List<CommentDTO> comments = commentService.getAllCommentByStatus();
          return ApiResponse.success(comments);
    }

    @Operation(summary = "审核评论状态", description = "审核评论状态,将status从0设置为1")
    @PutMapping("/comments/approve/{commentId}")
    public ApiResponse<String> updateCommentStatus(@PathVariable Long commentId){
         commentService.updateCommentStatus(commentId);
         return ApiResponse.success("审核成功");
    }
}
