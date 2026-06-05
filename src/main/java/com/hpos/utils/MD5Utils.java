package com.hpos.utils;

import cn.hutool.crypto.SecureUtil;

/**
 * MD5 加密工具类
 * <p>
 * 底层使用 Hutool 的 SecureUtil.md5()，比手工写 MessageDigest 更简洁可靠
 * </p>
 */
public class MD5Utils {

    /**
     * MD5 加密
     *
     * @param input 明文
     * @return 32 位小写 MD5
     */
    public static String encrypt(String input) {
        return SecureUtil.md5(input);
    }

    /**
     * 校验密码
     *
     * @param input     明文
     * @param encrypted 密文
     */
    public static boolean verify(String input, String encrypted) {
        return encrypt(input).equals(encrypted);
    }
}
