package com.hospital.controller.api;

import com.hospital.entity.User;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证API控制器
 * 提供用户登录认证相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null) {
                response.put("success", false);
                response.put("message", "用户名和密码不能为空");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 使用加密验证用户密码
            boolean isValid = userService.validatePassword(username, password);
            if (isValid) {
                User user = userService.getUserByUsername(username);
                
                // 将用户信息保存到session中，供其他控制器使用
                session.setAttribute("currentUser", user);
                
                response.put("success", true);
                response.put("message", "登录成功");
                response.put("user", user);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "用户名或密码错误");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "登录失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "登出成功");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从session中获取当前用户
            User currentUser = (User) session.getAttribute("currentUser");
            
            if (currentUser != null) {
                response.put("success", true);
                response.put("message", "用户已登录");
                response.put("user", currentUser);
                System.out.println("检查登录状态 - 当前用户: " + currentUser.getUsername() + ", 手机号: " + currentUser.getPhone());
            } else {
                response.put("success", false);
                response.put("message", "用户未登录");
                response.put("user", null);
            }
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "检查登录状态失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registerRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            String username = registerRequest.get("username");
            String password = registerRequest.get("password");
            String confirmPassword = registerRequest.get("confirmPassword");
            String phone = registerRequest.get("phone");

            // 验证必填字段
            if (username == null || username.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "用户名不能为空");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "密码不能为空");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "确认密码不能为空");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (phone == null || phone.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "手机号不能为空");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 验证密码一致性
            if (!password.equals(confirmPassword)) {
                response.put("success", false);
                response.put("message", "两次输入的密码不一致");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 创建用户对象
            User user = new User();
            user.setUsername(username.trim());
            user.setPassword(password.trim());
            user.setPhone(phone.trim());
            user.setRealName("用户" + username.trim()); // 自动生成默认真实姓名
            user.setRole("PATIENT");
            user.setEnabled(true);

            // 调用服务创建用户
            boolean success = userService.createUser(user);
            if (success) {
                response.put("success", true);
                response.put("message", "注册成功");
                response.put("user", user);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("success", false);
                response.put("message", "注册失败");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}