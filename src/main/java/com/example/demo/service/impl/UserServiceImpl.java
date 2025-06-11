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

    /**
     * 用户注册方法
     *
     * 此方法接收一个注册请求对象，对用户名和密码进行校验和处理，并创建新用户
     * 如果用户名不符合规范或已存在相同用户名，将抛出相应的异常
     *
     * @param registerRequest 注册请求对象，包含用户输入的用户名、密码和邮箱等信息
     */
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
        // 构建User对象并保存到数据库
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


    /**
     * 登录功能实现方法
     *
     * 该方法负责处理用户登录请求，验证用户身份，并返回用户相关信息
     * 它首先根据用户名从数据库中查找用户，如果用户不存在，则抛出用户不存在异常
     * 接着，使用密码编码器验证用户密码如果密码不匹配，则抛出无效用户名或密码异常
     * 当用户验证成功后，构建并返回用户响应对象，包含用户ID、用户名、邮箱和头像URL
     *
     * @param loginRequest 登录请求对象，包含用户名和密码
     * @return UserResponse 用户响应对象，包含用户相关信息
     * @throws UserException 如果用户不存在或密码验证失败，则抛出用户异常
     */
    @Override
    public UserResponse login(LoginRequest loginRequest) {
        // 根据用户名查找用户
        User user = userRepository.findByUsername(loginRequest.username());
        // 如果用户不存在，抛出用户不存在异常
        if (user == null) {
            throw new UserException(UserError.USER_NOT_EXIST);
        }

        // 验证用户密码
        boolean judge = passwordEncoder.matches(loginRequest.password(), user.getPassword());

        // 如果密码验证失败，抛出无效用户名或密码异常
        if (!judge){
            throw new UserException(UserError.INVALID_PASSWD_USERNAME);
        }

        // 构建并返回用户响应对象
        return  UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    /**
     * 更新用户信息
     *
     * 此方法根据提供的登录请求和用户ID来更新用户信息如果用户不存在，将抛出用户不存在的异常
     * 如果尝试更改用户名为已存在的用户名，将抛出用户名重复的异常如果用户名格式不正确，将抛出非法用户名的异常
     * 如果提供了新密码，将对新密码进行加密并更新
     *
     * @param request 包含要更新的用户信息的登录请求
     * @param id 要更新的用户的ID
     */
    @Override
    public void update(LoginRequest request, Long id) {
        // 获取指定ID的用户，如果不存在则抛出用户不存在的异常
        User user = userRepository.findById(id).orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));

        // 用户名是否存在
        if (request.username() != null && !request.username().isBlank()){
            // 检查新用户名是否与当前用户名不同且已存在于数据库中，如果是，则抛出用户名重复的异常
            if (!request.username().equals(user.getUsername()) && userRepository.existsByUsername(request.username())){
                throw new UserException(UserError.DUPLICATE_USERNAME);
            }
            // 检查用户名格式是否正确，如果不正确，则抛出非法用户名的异常
            if (!request.username().matches("[a-zA-Z0-9_]{3,15}")) {
                throw new UserException(UserError.INVALID_USERNAME); //抛出1001, "非法用户名"异常处理
            }
            // 更新用户名
            user.setUsername(request.username());
        }

        // 密码是否修改
        if (request.password() != null && !request.password().isBlank()){
            // 对新密码进行加密并更新
            String encodedPassword = passwordEncoder.encode(request.password());
            user.setPassword(encodedPassword);
        }

        // 保存更新后的用户信息
        userRepository.save(user);
    }

    /**
     * 存储用户上传的文件
     *
     * @param userId 用户ID，用于关联文件与特定用户
     * @param file 用户上传的文件，包含文件内容和文件名等信息
     * @return 返回存储后的文件路径，以便用户访问
     *
     * 此方法负责处理用户上传的文件，包括验证文件有效性、生成新的文件名、
     * 创建必要的上传目录、将文件保存到服务器、更新用户头像URL以及返回文件的访问路径
     */
    @Override
    public String storeFile(Long userId, MultipartFile file) {
        try {
            // 检查上传的文件是否为空，如果为空则抛出业务异常
            if (file.isEmpty()) throw new UserException(UserError.INVALID_FILE);

            // 获取文件扩展名，用于生成新的文件名
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            // 使用UUID生成唯一的文件名，避免文件名冲突
            String randomFileName = UUID.randomUUID() + "." + extension;

            // 获取上传目录的路径
            Path uploadPath = Paths.get(this.uploadDir);
            // 检查上传目录是否存在，如果不存在则创建
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            // 解析最终的文件保存路径
            Path filePath = uploadPath.resolve(randomFileName);
            // 将上传的文件转移到指定路径
            file.transferTo(filePath);

            // 构造文件的访问路径，用于后续返回或存储
            String path = accessPath.replace("**", randomFileName);

            // 根据用户ID获取用户实体，如果用户不存在则抛出业务异常
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));
            // 更新用户的头像URL，并保存用户实体
            user.setAvatarUrl(path);
            userRepository.save(user);

            // 返回文件的访问路径
            return path;
        } catch (UserException e) {
            // 显式重新抛出业务异常，以便上层处理
            throw e;
        } catch (Exception e) {
            // 如果发生其他异常，则返回上传失败的消息
            return "上传失败";
        }
    }

    /**
     * 获取所有用户信息
     *
     * 此方法从数据库中检索所有用户，并将它们转换为UserResponse对象列表
     * 主要用于需要获取系统中所有用户信息的场景，例如用户管理模块
     *
     * @return 包含所有用户信息的UserResponse对象列表
     */
    @Override
    public List<UserResponse> getAllUser() {
        // 使用userRepository的findAll方法获取数据库中的所有User对象
        List<User> users = userRepository.findAll();

        // 利用Java Stream API将获取到的User对象列表转换为UserResponse对象列表
        // 这里使用map函数对每个User对象进行转换，提取出需要的信息并封装到UserResponse对象中
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAvatarUrl()))
                .toList();
    }

    /**
     * 根据用户ID获取用户信息
     * 此方法覆盖自上级类或接口，旨在实现根据用户ID查询用户的功能
     * 如果用户不存在，则抛出UserException异常，指示用户不存在
     *
     * @param id 用户ID，用于查询用户信息
     * @return User对象，包含用户信息
     * @throws UserException 当用户ID不存在时，抛出此异常
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException(UserError.USER_NOT_EXIST));
    }

    /**
     * 根据用户ID删除用户
     *
     * 此方法接收一个Long类型的用户ID，通过调用userRepository的deleteById方法来删除指定的用户
     * 它没有返回值，因为delete操作通常不返回结果
     *
     * @param id 用户ID，用于标识要删除的用户
     */
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
