package com.filerepository.common.aspect;

import com.filerepository.common.annotation.LogExecutionTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * AOP aspect for monitoring method execution time
 */
@Aspect
@Component
@Slf4j
public class PerformanceMonitoringAspect {
    
    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        log.info("Method {}.{} executed in {} ms", 
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), 
                executionTime);
        
        if (executionTime > 1000) {
            log.warn("PERFORMANCE ALERT: Method {}.{} took more than 1 second to execute: {} ms", 
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), 
                    executionTime);
        }
        
        return result;
    }
}
