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
public class ServiceUpdateAspect {

    @Pointcut("execution (public * com.dima.service.*Service.update(*,*))")
    public void anyUpdateServiceMethod() {
    }

    @Before(value = "anyUpdateServiceMethod() && args(id, dto) && target(service)", argNames = "id,dto,service")
    public void addLoggingUpdate(Object id, Object dto, Object service) {
        log.info("invoke update method in class {}, with id {} and dto {}", service, id, dto);
    }

    @AfterReturning(value = "anyUpdateServiceMethod() && target(service)", returning = "result", argNames = "service,result")
    public void addLoggingAfterReturningUpdate(Object service, Object result) {
        log.info("after returning - invoke update method in class {}, result {}", service, result);
    }
}
