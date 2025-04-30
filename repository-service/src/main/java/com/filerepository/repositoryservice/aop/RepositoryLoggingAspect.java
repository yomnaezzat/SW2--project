package com.filerepository.repositoryservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RepositoryLoggingAspect {

    @Pointcut("execution(* com.filerepository.repositoryservice.controller.*.*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.filerepository.repositoryservice.service.*.*(..))")
    public void serviceMethods() {}

    @Before("controllerMethods()")
    public void logBeforeController(JoinPoint joinPoint) {
        log.info("Repository Controller Method Called: {} with arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result) {
        log.info("Repository Controller Method Finished: {} with result: {}",
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logExceptionController(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in Repository Controller Method: {} with message: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }

    @Around("serviceMethods()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        log.info("Repository Service Method Started: {} with arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.info("Repository Service Method Finished: {} with result: {} in {} ms",
                joinPoint.getSignature().getName(),
                result,
                endTime - startTime);

        return result;
    }
}