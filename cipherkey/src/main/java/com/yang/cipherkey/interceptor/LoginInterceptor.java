package com.yang.cipherkey.interceptor;


import com.yang.home.common.utill.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


// 交给容器去管理,帮忙注入 secret
// 采用对称加密了,所以不用这个东西了
public class LoginInterceptor implements HandlerInterceptor {

    public static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    private String secret; // 注册拦截器时配置好token

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            //获取请求头
        logger.debug("拦截请求!");
        String token = request.getHeader("token");
        if(token==null){
            response.setStatus(401);
            return false;
        }
        logger.debug("验证token:{}",token);
        Map<String,Object> map = JwtUtil.validateToken(token,secret );
        return true;
    }
  }
