package com.yang.mytool.config;

import com.yang.home.common.interceptor.MDCInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器 - 拦截所有请求
        registry.addInterceptor(new MDCInterceptor())
                .addPathPatterns("/**")//拦截所有的 url
//                .excludePathPatterns("/user/login")//排除的url
                .order(0); // 最先执行 越小越先执行

        // 不用登录,只提供接口,在本地解密即可
//        LoginInterceptor loginInterceptor = new LoginInterceptor();
//        loginInterceptor.setSecret(secret);
//        log.debug("secret:{}", secret);
//        log.debug("path:{}", contextPath+"/login");
//        registry.addInterceptor(loginInterceptor)
//                // 配置contextPath 就不要写完整路径了,否则不生效
//                .excludePathPatterns("/login")
//                .addPathPatterns("/**");

    }
}