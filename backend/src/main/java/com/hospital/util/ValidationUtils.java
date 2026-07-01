package com.hospital.util;

import java.util.function.Function;

/**
 * 验证工具类
 * 提供通用的验证方法
 */
public class ValidationUtils {

    /**
     * 验证名称是否存在
     *
     * @param name          名称
     * @param entityType    实体类型（用于错误消息）
     * @param existsChecker 存在性检查函数
     * @throws RuntimeException 如果名称已存在
     */
    public static void validateNameExists(String name, String entityType, Function<String, Boolean> existsChecker) {
        if (name != null && existsChecker.apply(name.trim())) {
            throw new RuntimeException(entityType + "名称已存在");
        }
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        // 简单的手机号格式验证：1开头，11位数字
        return phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * 验证用户名格式
     *
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        // 用户名：字母开头，允许字母、数字、下划线，3-20位
        return username.matches("^[a-zA-Z][a-zA-Z0-9_]{2,19}$");
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        // 密码：至少6位，包含字母和数字
        return password.length() >= 6 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }

    /**
     * 验证身份证号格式
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCard(String idCard) {
        if (idCard == null) return false;
        // 简单的身份证号格式验证：15位或18位数字，最后一位可以是X
        return idCard.matches("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$") ||
                idCard.matches("^[1-9]\\d{7}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}$");
    }
}