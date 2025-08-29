package com.yang.home.interceptor;

import com.yang.home.utils.LogReqIdUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MDCInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在控制器执行之前调用
        LogReqIdUtil.setMDCLogReqId();
        return true; // 继续执行
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // 控制器执行完毕之后，渲染视图之前
//    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 渲染完成，准备返回
        LogReqIdUtil.removeMDCLogReqId();
    }

}