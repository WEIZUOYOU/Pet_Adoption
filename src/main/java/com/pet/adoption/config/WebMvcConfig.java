package com.pet.adoption.config;

import com.pet.adoption.interceptor.AdminInterceptor;
import com.pet.adoption.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.path:./uploads}")
    private String uploadPath;

    @Value("${app.upload.url-prefix:/uploads}")
    private String urlPrefix;

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
                .addPathPatterns("/api/adoption/**", 
                                "/api/comment/**",
                                "/api/user/current")   // 新增
                .excludePathPatterns("/api/user/login", 
                                    "/api/user/register", 
                                    "/api/pet/**");
        
        // 管理员权限拦截
        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/api/admin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射上传文件目录到 /uploads/** URL路径
        String os = System.getProperty("os.name").toLowerCase();
        String path;
        
        if (os.contains("win")) {
            // Windows系统：转换为绝对路径
            path = "file:" + new java.io.File(uploadPath).getAbsolutePath() + "/";
        } else {
            // Linux/Mac系统
            path = "file:" + uploadPath + "/";
        }
        
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations(path);
    }
}