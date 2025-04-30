package com.filerepository.fileservice.aspect;

import com.filerepository.common.annotation.Audited;
import com.filerepository.fileservice.model.FileHistory;
import com.filerepository.fileservice.repository.FileHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class FileAuditingAspect {

    private final FileHistoryRepository fileHistoryRepository;

    @AfterReturning("@annotation(com.filerepository.common.annotation.Audited) && args(fileId, userId, ..)")
    public void logFileOperation(JoinPoint joinPoint, Long fileId, Long userId) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Audited auditedAnnotation = method.getAnnotation(Audited.class);
            
            String actionType = auditedAnnotation.action();
            String username = "system"; // Ideally, get from security context or service
            
            // Create file history entry
            FileHistory fileHistory = FileHistory.builder()
                    .fileId(fileId)
                    .actionType(actionType)
                    .actionDescription(method.getName() + " operation")
                    .userId(userId)
                    .username(username)
                    .timestamp(LocalDateTime.now())
                    .build();
            
            fileHistoryRepository.save(fileHistory);
            log.debug("File operation logged: {}", fileHistory);
        } catch (Exception e) {
            log.error("Error logging file operation", e);
        }
    }

    @AfterReturning("@annotation(com.filerepository.common.annotation.Audited) && execution(* com.filerepository.fileservice.service.FileService.uploadFile(..))")
    public void logFileUpload(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long repositoryId = (Long) args[1];
            Long folderId = (Long) args[2];
            Long uploaderId = (Long) args[3];
            
            log.info("File uploaded to repository={}, folder={} by user={}", 
                    repositoryId, folderId, uploaderId);
        } catch (Exception e) {
            log.error("Error logging file upload", e);
        }
    }
}
