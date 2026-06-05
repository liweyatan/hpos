package com.hpos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Web 配置类
 * <p>
 * 功能：配置跨域（CORS），允许前端 Vue 开发服务器访问后端 API
 * </p>
 */
@Configuration
public class WebConfig {

    /**
     * 跨域过滤器
     * <p>
     * 前端开发时 Vite 默认运行在 localhost:5173，
     * 需要允许来自该地址的跨域请求
     * </p>
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许携带凭证（Cookie、Authorization 头）
        config.setAllowCredentials(true);
        // 允许的域名（* 表示所有，生产环境请指定具体域名）
        config.addAllowedOriginPattern("*");
        // 允许的请求头
        config.addAllowedHeader("*");
        // 允许的请求方法
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径生效
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
