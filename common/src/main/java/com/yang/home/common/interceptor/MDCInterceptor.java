package com.yang.home.common.interceptor;

import com.yang.home.common.utill.LogReqIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MDCInterceptor implements HandlerInterceptor {
    public static Logger logger = LoggerFactory.getLogger(MDCInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在控制器执行之前调用
        LogReqIdUtil.setMDCLogReqId();
        logger.debug("生成日志链路追踪id!");
        return true; // 继续执行
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // 控制器执行完毕之后，渲染视图之前
//    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("移除日志链路追踪id!");
        // 渲染完成，准备返回
        LogReqIdUtil.removeMDCLogReqId();

    }

}