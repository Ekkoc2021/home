package com.yang.home.config;

import com.yang.home.interceptor.MDCInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器 - 拦截所有请求
        registry.addInterceptor(new MDCInterceptor())
                .addPathPatterns("/**")//拦截所有的 url
//                .excludePathPatterns("/user/login")//排除的url
                .order(0); // 最先执行 越小越先执行

    }
}