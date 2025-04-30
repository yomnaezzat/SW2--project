package com.filerepository.userservice.aspect;

import com.filerepository.common.annotation.Audited;
import com.filerepository.userservice.model.AuditLog;
import com.filerepository.userservice.service.AuditLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Arrays;

@Aspect
@Component
public class AuditingAspect {
    
    private final AuditLogService auditLogService;
    
    public AuditingAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    
    @Around("@annotation(com.filerepository.common.annotation.Audited)")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get method details
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Audited auditedAnnotation = method.getAnnotation(Audited.class);
        
        String action = auditedAnnotation.action();
        String resource = auditedAnnotation.resource();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        String arguments = Arrays.toString(joinPoint.getArgs());
        
        // Extract user from SecurityContext if available
        String username = "system";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
        }
        
        LocalDateTime startTime = LocalDateTime.now();
        Object result = null;
        boolean success = false;
        String errorMessage = null;
        
        try {
            result = joinPoint.proceed();
            success = true;
            return result;
        } catch (Throwable e) {
            errorMessage = e.getMessage();
            throw e;
        } finally {
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(startTime, endTime);
            
            AuditLog auditLog = AuditLog.builder()
                .action(action)
                .resource(resource)
                .username(username)
                .className(className)
                .methodName(methodName)
                .arguments(arguments)
                .timestamp(startTime)
                .durationMs(duration.toMillis())
                .success(success)
                .errorMessage(errorMessage)
                .build();
            
            auditLogService.saveAuditLog(auditLog);
        }
    }
}
