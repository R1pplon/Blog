package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.User;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    // 注册
    void register(RegisterRequest registerRequest);

    // 登录
    UserResponse login(LoginRequest loginRequest);

    // 修改
    void update(@Valid LoginRequest request, Long id);

    String storeFile(Long id, MultipartFile file);

    List<UserResponse> getAllUser();

    User getUserById(Long id);

    void deleteUserById(Long id);
}
