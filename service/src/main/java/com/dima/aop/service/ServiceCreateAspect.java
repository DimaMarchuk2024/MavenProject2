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
public class ServiceCreateAspect {

    @Pointcut("execution (public * com.dima.service.*Service.create(*))")
    public void anyCreateServiceMethod() {
    }

    @Before(value = "anyCreateServiceMethod() && args(dto) && target(service)", argNames = "dto,service")
    public void addLoggingCreate(Object dto, Object service) {
        log.info("invoke create method in class {}, with dto {}", service, dto);
    }

    @AfterReturning(value = "anyCreateServiceMethod() && target(service)", returning = "result", argNames = "service,result")
    public void addLoggingAfterReturningCreate(Object service, Object result) {
        log.info("after returning - invoke create method in class {}, result {}", service, result);
    }
}
