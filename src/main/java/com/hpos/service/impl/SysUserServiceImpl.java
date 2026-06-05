package com.hpos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpos.entity.SysUser;
import com.hpos.mapper.SysUserMapper;
import com.hpos.service.SysUserService;
import com.hpos.utils.MD5Utils;
import org.springframework.stereotype.Service;

/**
 * 系统用户 Service 实现类
 */
@Service
public class SysUserServiceImpl
        extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    /**
     * 用户登录
     * 1. 根据用户名查找用户
     * 2. 将明文密码做 MD5 加密后与数据库比对
     */
    @Override
    public SysUser login(String username, String password) {
        // 查找用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = this.getOne(wrapper);

        if (user == null) {
            return null; // 用户名不存在
        }

        // 校验密码（前端传入的明文密码 MD5 后比对）
        if (!MD5Utils.encrypt(password).equals(user.getPassword())) {
            return null; // 密码错误
        }

        return user;
    }

    /**
     * 根据用户名查找用户信息
     */
    @Override
    public SysUser findByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return this.getOne(wrapper);
    }
}
