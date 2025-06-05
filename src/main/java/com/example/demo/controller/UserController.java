package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.CookieUtil;
import com.example.demo.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "用户相关Api", description = "用于登录和注册")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Operation(summary = "获取用户信息", description = "获取当前用户的详细信息")
    @GetMapping({"/info"})
    public ApiResponse<UserResponse> update(@RequestAttribute("userId") Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return ApiResponse.success(UserResponse.builder()
                .id(id)
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .build());
    }


    @Operation(summary = "更改用户信息", description = "增量更新, 可以传入一个或者多个值, 传入的数据对应的字段如果不为空, 就更新他")
    @PutMapping({"/info"})
    public ApiResponse<UserResponse> update(@RequestBody @Valid LoginRequest request,
                                            @RequestAttribute("userId") Long id) {
        userService.update(request, id);
        User user = userRepository.findById(id).orElseThrow();
        UserResponse userResponse = UserResponse.builder()
                .id(id)
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .build();
         return ApiResponse.success(userResponse);
    }

    @Operation(summary = "用于上传头像", description = "传入一个 id, 用于指定传入的头像归属于那个用户. 一个图片文件作为头像. 记得设置 content-type 为 multipart/form-data")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file,
                                      @RequestAttribute("userId") Long id) throws IOException {
        String msg = userService.storeFile(id, file);
        if (msg.equals("上传失败")){
            return ApiResponse.error(500, msg);
        }
        return ApiResponse.success(msg);
    }

    @Operation(summary = "登出", description = "退出登录, 删除 cookie")
    @DeleteMapping("/logout")
    public ApiResponse<String> logout(HttpServletResponse response) {
        CookieUtil.deleteCookie(response);
        return ApiResponse.success("退出登录成功");
    }
}
