package com.hpos.common;

import com.hpos.dto.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * <p>
 * 使用 @RestControllerAdvice 统一捕获所有 Controller 抛出的异常，
 * 避免将堆栈信息直接暴露给前端
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获参数校验异常（@Valid 或 @Validated 校验失败时触发）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidation(MethodArgumentNotValidException e) {
        // 获取第一条校验失败消息
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ApiResponse.error(400, message);
    }

    /**
     * 捕获业务运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleRuntime(RuntimeException e) {
        return ApiResponse.error(500, e.getMessage());
    }

    /**
     * 捕获所有未处理的异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
    }
}
