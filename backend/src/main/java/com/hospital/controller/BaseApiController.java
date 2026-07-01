package com.hospital.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础API控制器
 * 提供统一的错误处理和响应格式
 */
@RestControllerAdvice
public class BaseApiController {

    /**
     * 创建成功响应
     *
     * @param message 成功消息
     * @param data    响应数据
     * @return 统一格式的成功响应
     */
    protected ResponseEntity<Map<String, Object>> successResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 创建成功响应（无数据）
     *
     * @param message 成功消息
     * @return 统一格式的成功响应
     */
    protected ResponseEntity<Map<String, Object>> successResponse(String message) {
        return successResponse(message, null);
    }

    /**
     * 创建错误响应
     *
     * @param message 错误消息
     * @param status  HTTP状态码
     * @return 统一格式的错误响应
     */
    protected ResponseEntity<Map<String, Object>> errorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }

    /**
     * 处理异常
     *
     * @param e 异常对象
     * @return 统一格式的错误响应
     */
    protected ResponseEntity<Map<String, Object>> handleException(Exception e) {
        return errorResponse("操作失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理运行时异常
     *
     * @param e 运行时异常对象
     * @return 统一格式的错误响应
     */
    protected ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        return errorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 验证必填字段
     *
     * @param fieldValue 字段值
     * @param fieldName  字段名称
     * @throws RuntimeException 如果字段为空或空字符串
     */
    protected void validateRequiredField(String fieldValue, String fieldName) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            throw new RuntimeException(fieldName + "不能为空");
        }
    }
}