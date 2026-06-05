package com.hpos.controller;

import com.hpos.dto.ApiResponse;
import com.hpos.dto.LoginRequest;
import com.hpos.entity.SysUser;
import com.hpos.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录认证 API
 * 
 * 当前使用简单的用户名+密码验证（MD5 加密比对），
 * 后续接入 JWT + Shiro 后，这里会改为 token 鉴权。
 * 
 * <h3>接口：</h3>
 * <pre>
 * POST /api/auth/login      → 登录
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

    /**
     * 用户登录
     * 
     * <h4>请求体：</h4>
     * <pre>
     * { "username": "admin", "password": "123456" }
     * </pre>
     * 
     * <h4>成功返回：</h4>
     * <pre>
     * { "code": 200, "message": "登录成功", "data": { "id": 3, "username": "admin", "phone": "13800138000" } }
     * </pre>
     * 注意：返回数据中 password 字段已被清空，不会暴露加密密码
     * 
     * <h4>失败返回：</h4>
     * <pre>
     * { "code": 401, "message": "用户名或密码错误", "data": null }
     * </pre>
     * 
     * @param request 登录参数（username + password），已做非空校验
     */
    @PostMapping("/login")
    public ApiResponse<SysUser> login(@Valid @RequestBody LoginRequest request) {
        SysUser user = userService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            // 不区分"用户名不存在"和"密码错误"，防止暴力枚举用户名
            return ApiResponse.error(401, "用户名或密码错误");
        }
        // 返回前清除密码，防止密码泄露
        user.setPassword(null);
        return ApiResponse.success("登录成功", user);
    }

    /**
     * 获取用户信息
     * 
     * GET /api/auth/user/admin
     * 
     * @param username 用户名（URL路径参数）
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
