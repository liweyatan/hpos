package com.hpos.controller;

import com.hpos.dto.ApiResponse;
import com.hpos.dto.LoginRequest;
import com.hpos.dto.LoginResponse;
import com.hpos.entity.SysUser;
import com.hpos.security.JwtUtil;
import com.hpos.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录认证 API
 * 
 * 接入 JWT + Spring Security，登录后返回 token，后续请求需在 Header 携带：
 * Authorization: Bearer <token>
 * 
 * <h3>接口：</h3>
 * <pre>
 * POST /api/auth/login      → 登录，返回 JWT token
 * GET  /api/auth/user/{xxx} → 获取用户信息
 * </pre>
 * 
 * <h3>测试账号（来自 HPOS.SQL 的测试数据）：</h3>
 * <pre>
 * admin    / 123456
 * zhangsan / 123456
 * lisi     / 123456
 * </pre>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录（返回 JWT Token）
     * 
     * <h4>请求体：</h4>
     * <pre>
     * { "username": "admin", "password": "123456" }
     * </pre>
     * 
     * <h4>成功返回：</h4>
     * <pre>
     * {
     *   "code": 200,
     *   "message": "登录成功",
     *   "data": {
     *     "token": "eyJhbGciOiJIUzI1NiJ9...",
     *     "userId": 3,
     *     "username": "admin",
     *     "phone": "13800138000"
     *   }
     * }
     * </pre>
     * 
     * <h4>后续请求需在 Header 中携带 Token：</h4>
     * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
     * 
     * @param request 登录参数（username + password）
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        SysUser user = userService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ApiResponse.error(401, "用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        LoginResponse loginResponse = new LoginResponse(token, user.getId(), user.getUsername(), user.getPhone());
        return ApiResponse.success("登录成功", loginResponse);
    }

    /**
     * 获取用户信息
     * 
     * GET /api/auth/user/{username}
     */
    @GetMapping("/user/{username}")
    public ApiResponse<SysUser> getUser(@PathVariable String username) {
        SysUser user = userService.findByUsername(username);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        user.setPassword(null);
        return ApiResponse.success(user);
    }
}
