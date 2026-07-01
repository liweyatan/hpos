package com.hospital.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源配置类
 * 同时支持Vue SPA前端和JSP静态资源
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Vue SPA静态资源（index.html + assets/）
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // JSP项目静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "file:src/main/webapp/static/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/", "file:src/main/webapp/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/", "file:src/main/webapp/static/js/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/", "file:src/main/webapp/static/images/");

        registry.addResourceHandler("/webapp/**")
                .addResourceLocations("file:src/main/webapp/");
    }
}