package com.hpos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpos.entity.SysUser;

/**
 * 系统用户 Service 接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录验证
     *
     * @param username 用户名
     * @param password 明文密码（方法内会做 MD5 加密再比对）
     * @return 用户信息（失败返回 null）
     */
    SysUser login(String username, String password);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser findByUsername(String username);
}
