package com.pet.adoption.config;

import com.pet.adoption.interceptor.AdminInterceptor;
import com.pet.adoption.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截：需要登录才能访问的接口
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/api/adoption/**", "/api/comment/**")
                .excludePathPatterns("/api/user/**", "/api/pet/**");

        // 管理员权限拦截
        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/api/admin/**");
    }
}