package com.filerepository.repositoryservice.aspect;

import com.filerepository.common.annotation.Audited;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RepositoryAuditingAspect {

    @AfterReturning("@annotation(com.filerepository.common.annotation.Audited)")
    public void logRepositoryOperation(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Audited auditedAnnotation = method.getAnnotation(Audited.class);
            
            String actionType = auditedAnnotation.action();
            String resource = auditedAnnotation.resource();
            
            Object[] args = joinPoint.getArgs();
            StringBuilder argsStr = new StringBuilder();
            for (Object arg : args) {
                if (arg != null) {
                    argsStr.append(arg.toString().length() > 100 
                            ? arg.toString().substring(0, 100) + "..." 
                            : arg.toString());
                    argsStr.append(", ");
                } else {
                    argsStr.append("null, ");
                }
            }
            
            if (argsStr.length() > 2) {
                argsStr.setLength(argsStr.length() - 2);
            }
            
            log.info("Repository operation - Action: {}, Resource: {}, Method: {}.{}, Args: [{}]", 
                    actionType, 
                    resource, 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    method.getName(),
                    argsStr);
        } catch (Exception e) {
            log.error("Error in repository auditing aspect", e);
        }
    }
}
