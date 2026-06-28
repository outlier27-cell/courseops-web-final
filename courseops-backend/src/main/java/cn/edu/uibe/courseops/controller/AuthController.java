package cn.edu.uibe.courseops.controller;

import cn.edu.uibe.courseops.auth.JwtService;
import cn.edu.uibe.courseops.common.BusinessException;
import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.dto.LoginRequest;
import cn.edu.uibe.courseops.dto.LoginResponse;
import cn.edu.uibe.courseops.entity.UserEntity;
import cn.edu.uibe.courseops.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var user = userMapper.selectById(request.userId());
        if (user == null || !passwordEncoder.matches(request.password(), user.password)) {
            throw new BusinessException("账号或密码错误");
        }
        user.password = "";
        return Result.ok(new LoginResponse(jwtService.issueToken(user.id, user.role), user));
    }

    @GetMapping("/me")
    public Result<UserEntity> me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        UserEntity user = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                Claims claims = jwtService.parseToken(authorization.substring(7));
                user = userMapper.selectById(claims.getSubject());
            } catch (Exception ignored) {
                user = null;
            }
        }
        if (user != null) {
            user.password = "";
        }
        return Result.ok(user);
    }

    @GetMapping("/users")
    public Result<Object> users() {
        var users = userMapper.selectList(null);
        users.forEach(user -> user.password = "");
        return Result.ok(users);
    }
}
