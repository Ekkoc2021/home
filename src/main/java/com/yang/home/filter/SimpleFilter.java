package com.yang.home.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// 过滤器java web级别(tomcat)的api,拦截器属于spring级别的api ==>过滤器能实现的拦截器都能实现
// 一般过滤器执行先于拦截器
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 越小优先级越高,也可以用Ordered的枚举
public class SimpleFilter implements Filter { // 实现filter接口,通过component交给spring容器管理即可,也可以通过配置类注册
    static Logger logger= LoggerFactory.getLogger(SimpleFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法，在过滤器创建时调用
        logger.debug("SimpleFilter 初始化");
    }

    // 过滤器核心方法
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // 预处理逻辑（在请求到达控制器之前）
        logger.debug("请求URL: " + httpRequest.getRequestURL());
        logger.debug("请求方法: " + httpRequest.getMethod());
        
        // 继续处理请求（如果不调用此方法，请求将被阻断）
        chain.doFilter(request, response);
        
        // 后处理逻辑（在响应返回客户端之前）
        logger.debug("响应已处理");
    }

    @Override
    public void destroy() {
        // 销毁方法，在过滤器销毁时调用
        logger.debug("SimpleFilter 销毁");
    }
}