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
public class ServiceFindByIdAspect {

    @Pointcut("execution (public * com.dima.service.*Service.findById(*))")
    public void anyFindByIdServiceMethod() {
    }

    @Before(value = "anyFindByIdServiceMethod() && args(id) && target(service)", argNames = "id,service")
    public void addLoggingFindById(Object id, Object service) {
        log.info("invoke findById method in class {}, with id {}", service, id);
    }

    @AfterReturning(value = "anyFindByIdServiceMethod() && target(service)", returning = "result", argNames = "service,result")
    public void addLoggingAfterReturningFindById(Object service, Object result) {
        log.info("after returning - invoke findById method in class {}, result {}", service, result);
    }
}
