package com.Travel.aop;

import com.Travel.annotation.AuthCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthInterceptor {

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        System.out.println(authCheck);
        if(authCheck.equals("admin")){
            throw new RuntimeException("6666");
        }
        return joinPoint.proceed();
    }
}
