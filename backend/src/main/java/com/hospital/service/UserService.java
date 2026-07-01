package com.hospital.service;

import com.hospital.entity.User;
import com.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户信息服务类
 * 处理用户相关的业务逻辑
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据用户名获取用户信息
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据ID获取用户信息
     */
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 创建新用户
     */
    public boolean createUser(User user) {
        // 检查用户名是否已存在
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 直接使用明文密码，不进行加密
        // 执行插入
        return userRepository.insert(user) > 0;
    }

    /**
     * 更新用户信息
     */
    public boolean updateUser(User user) {
        return userRepository.update(user) > 0;
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id) > 0;
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 验证用户密码
     */
    public boolean validatePassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword() != null &&
                user.getPassword().equals(password);
    }
}