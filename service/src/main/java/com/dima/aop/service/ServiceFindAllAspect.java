package com.dima.aop.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceFindAllAspect {

    @Pointcut("execution (public * com.dima.service.*Service.findAll(..))")
    public void anyFindAllServiceMethod() {
    }

    @Before(value = "anyFindAllServiceMethod() && args(any, ..)  && target(service)", argNames = "service,any")
    public void addLoggingFindAll(Object service, Object any) {
        log.info("invoke findAll method in class {}, with param {}", service, any);
    }

    @AfterReturning(value = "anyFindAllServiceMethod() && target(service)", returning = "result", argNames = "service,result")
    public void addLoggingAfterReturningFindAll(Object service, Object result) {
        log.info("after returning - invoke findAll method in class {}, result {}", service, result);
    }
}
