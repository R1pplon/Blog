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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${file.access-path}")
    private String accessPath;

    @Value("${file.upload-dir}")//将图片保存在本地c磁盘下
    private String uploadDir;

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
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    @Override
    public void update(LoginRequest request, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));

        // 用户名是否存在
        if (request.username() != null && !request.username().isBlank()){
            if (!request.username().equals(user.getUsername()) && userRepository.existsByUsername(request.username())){
                throw new UserException(UserError.DUPLICATE_USERNAME);
            }
            if (!request.username().matches("[a-zA-Z0-9_]{3,15}")) {
                throw new UserException(UserError.INVALID_USERNAME); //抛出1001, "非法用户名"异常处理
            }
            user.setUsername(request.username());
        }

        // 密码是否修改
        if (request.password() != null && !request.password().isBlank()){
            String encodedPassword = passwordEncoder.encode(request.password());
            user.setPassword(encodedPassword);
        }

        userRepository.save(user);
    }

    @Override
    public String storeFile(Long userId, MultipartFile file) {
        try {
            if (file.isEmpty()) throw new UserException(UserError.INVALID_FILE);

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String randomFileName = UUID.randomUUID() + "." + extension;

            Path uploadPath = Paths.get(this.uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(randomFileName);
            file.transferTo(filePath);

            String path = accessPath.replace("**", randomFileName);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));
            user.setAvatarUrl(path);
            userRepository.save(user);

            return path;
        } catch (UserException e) {
            throw e; // 显式重新抛出业务异常
        } catch (Exception e) {
            return "上传失败";
        }
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAvatarUrl()))
                .toList();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
