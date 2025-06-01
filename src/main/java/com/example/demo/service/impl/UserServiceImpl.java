package com.example.demo.service.impl;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.UserException;
import com.example.demo.exception.errors.UserError;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(RegisterRequest registerRequest) {
        // 如果注册用户名使用非法字符
        String username = registerRequest.username();
        if (!username.matches("[a-zA-Z0-9_]{3,15}")) {
            throw new UserException(UserError.INVALID_USERNAME); //抛出1001, "非法用户名"异常处理
        }
        // 如果存在相同的用户名
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new UserException(UserError.DUPLICATE_USERNAME); //抛出异常处理
        }
        // md5 加密密码
        String encodedPassword = passwordEncoder.encode(registerRequest.password());
        User user = User.builder()
                .username(registerRequest.username())
                .password(encodedPassword)
                .email(registerRequest.email())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .status(1)
                .role(1)
                .build();
        userRepository.save(user);
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username());
        if (user == null) {
            throw new UserException(UserError.USER_NOT_EXIST);
        }

        boolean judge = passwordEncoder.matches(loginRequest.password(), user.getPassword());

        if (!judge){
            throw new UserException(UserError.INVALID_PASSWD_USERNAME);
        }

        return  UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
