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
public class ServiceDeleteAspect {

    @Pointcut("execution (public * com.dima.service.*Service.delete(*))")
    public void anyDeleteServiceMethod() {
    }

    @Before(value = "anyDeleteServiceMethod() && args(id) && target(service)", argNames = "id,service")
    public void addLoggingDelete(Object id, Object service) {
        log.info("invoke delete method in class {}, with id {}", service, id);
    }

    @AfterReturning(value = "anyDeleteServiceMethod() && target(service)", returning = "result", argNames = "service,result")
    public void addLoggingAfterReturningDelete(Object service, boolean result) {
        log.info("after returning - invoke delete method in class {}, result {}", service, result);
    }
}
