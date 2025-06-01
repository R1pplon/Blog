package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.UserResponse;

public interface UserService {

    // 注册
    void register(RegisterRequest registerRequest);

    // 登录
    UserResponse login(LoginRequest loginRequest);

}
