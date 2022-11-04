package com.example.demo.services;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author STEVE on 24/10/2022
 */
@Aspect
@Component
public class MainSerivceAOP {

    @Before(value = "execution(* com.example.demo.services.MainService.add(..)) and args(arg1, arg2)")
    public void beforeAdvice(JoinPoint joinPoint, String arg1, String arg2) {
        Object[] args = joinPoint.getArgs();
        System.out.println("ASPECT before === joint point:" + joinPoint.getSignature() + " arg1 = " + arg1 + " arg2 = " + arg2);

    }

    // match all method in main service
    @After(value = "execution(* com.example.demo.services.MainService.*(..)) and args(arg1, arg2)")
    public void afterAdvice(JoinPoint joinPoint, String arg1, String arg2) {

        System.out.println("ASPECT after === joint point:" + joinPoint.getSignature() + " arg1 = " + arg1 + " arg2 = " + arg2);
    }

    //环绕增强
    @Around(value = "execution(* com.example.demo.services.MainService.*(..))")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("我是环绕增强中的前置增强！");
        Object[] args = jp.getArgs();
        Object proceed = jp.proceed();//植入目标方法
        System.out.println("我是环绕增强中的后置增强！");
    }

}
