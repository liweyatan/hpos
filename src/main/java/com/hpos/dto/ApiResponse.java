package com.hpos.dto;

import lombok.Data;

/**
 * 统一 API 响应结果类
 * <p>
 * 所有 Controller 接口均返回此格式，确保前端统一处理
 * </p>
 *
 * @param <T> 数据类型
 */
@Data
public class ApiResponse<T> {

    /** 状态码（200=成功，其他=失败） */
    private int code;

    /** 提示消息 */
    private String message;

    /** 返回数据 */
    private T data;

    /**
     * 构造私有化，通过静态方法创建实例
     */
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ==================== 成功响应 ====================

    /** 成功（无返回数据） */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功", null);
    }

    /** 成功（带返回数据） */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    /** 成功（自定义消息 + 数据） */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    // ==================== 失败响应 ====================

    /** 失败（默认消息） */
    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(500, "操作失败", null);
    }

    /** 失败（自定义消息） */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }

    /** 失败（自定义状态码和消息） */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
