package com.hospital.controller.api;

import com.hospital.entity.User;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理API控制器
 * 提供用户相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户列表
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            response.put("success", true);
            response.put("data", users);
            response.put("count", users.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户列表失败：" + e.getMessage());
            response.put("data", new java.util.ArrayList<>());
            response.put("count", 0);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                response.put("success", true);
                response.put("data", user);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 创建新用户
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = userService.createUser(user);
            if (success) {
                response.put("success", true);
                response.put("message", "用户创建成功");
                response.put("data", user);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("success", false);
                response.put("message", "用户创建失败");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建用户失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            user.setId(id);
            boolean success = userService.updateUser(user);
            if (success) {
                response.put("success", true);
                response.put("message", "用户更新成功");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "用户更新失败");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新用户失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                response.put("success", true);
                response.put("message", "用户删除成功");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "用户删除失败");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除用户失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateUserStatus(@PathVariable("id") Long id, @RequestParam("enabled") Boolean enabled) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                user.setEnabled(enabled);
                boolean success = userService.updateUser(user);
                if (success) {
                    response.put("success", true);
                    response.put("message", enabled ? "用户启用成功" : "用户禁用成功");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
            response.put("success", false);
            response.put("message", "操作失败");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "操作失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}