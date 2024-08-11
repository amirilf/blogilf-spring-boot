package com.blogilf.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blogilf.blog.interceptor.RequestInterceptor;

@Configuration
public class Config implements WebMvcConfigurer{
    

    private final RequestInterceptor requestInterceptor;

    Config(RequestInterceptor requestInterceptor){
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }

}
