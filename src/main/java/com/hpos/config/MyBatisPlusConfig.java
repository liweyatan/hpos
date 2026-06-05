package com.hpos.config;

import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类
 * <p>
 * 当前使用 MyBatis-Plus 3.5.9（Spring Boot 3 专用版），
 * 分页拦截器在该版本中已内置，无需额外配置。
 * 如需使用分页查询（selectPage），直接传入 Page 参数即可。
 * </p>
 */
@Configuration
public class MyBatisPlusConfig {
    // MyBatis-Plus 3.5.9 自动配置了所需插件，无需手动注册
}
