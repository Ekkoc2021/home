package com.yang.home.aspect;

import com.mysql.cj.log.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

//    @Pointcut(value = "execution(* com.yang.home.*.*(..))")
    public void pointCut() {
    }

    /**
     *前置通知:引用pointcut
     *
     */
//    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        // 逻辑代码
    }

    private  static Logger logger = LoggerFactory.getLogger(LogAspect.class);
    // 环绕通知 ==>可以使用自定义注解的方式标准类或者具体方法实现更细粒度的通知配置
//    @Around("@annotation(org.springframework.web.bind.annotation.RestController)") 匹配方法上带有对应注解
    @Around("@within(org.springframework.web.bind.annotation.RestController)") // 匹配类上带有对应注解
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("进入方法:" + joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        logger.debug("方法执行耗时:" + elapsedTime);
        return result;
    }

    // 后置通知
//    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After executing: " + joinPoint.getSignature().getName());
    }

    // 返回通知
//    @AfterReturning(
//            pointcut = "serviceMethods()",
//            returning = "result"
//    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("Method " + joinPoint.getSignature().getName() +
                " executed successfully with result: " + result);
    }
    // 异常通知
//    @AfterThrowing(
//            pointcut = "serviceMethods()",
//            throwing = "ex"
//    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        System.out.println("Method " + joinPoint.getSignature().getName() +
                " threw exception: " + ex.getMessage());
    }


}
