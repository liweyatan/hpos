package com.hospital.repository;

import com.hospital.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户信息数据访问接口
 * 处理用户表的CRUD操作
 */
@Mapper
public interface UserRepository {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM user WHERE username = #{username} AND enabled = 1")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "email", column = "email"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "realName", column = "real_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    User findByUsername(String username);

    /**
     * 根据ID查询用户
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "email", column = "email"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "realName", column = "real_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    User findById(Long id);

    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM user ORDER BY create_time DESC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "email", column = "email"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "realName", column = "real_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<User> findAll();

    /**
     * 新增用户
     */
    @Insert("INSERT INTO user(username, password, role, email, phone, real_name, enabled) " +
            "VALUES(#{username}, #{password}, #{role}, #{email}, #{phone}, #{realName}, #{enabled})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 更新用户信息
     */
    @Update("UPDATE user SET email=#{email}, phone=#{phone}, real_name=#{realName}, " +
            "enabled=#{enabled}, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int update(User user);

    /**
     * 删除用户（软删除）
     */
    @Update("UPDATE user SET enabled=0, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int deleteById(Long id);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND id != #{excludeId}")
    int countByUsername(String username, Long excludeId);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE email = #{email} AND id != #{excludeId}")
    int countByEmail(String email, Long excludeId);
}