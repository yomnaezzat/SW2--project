package com.filerepository.userservice.service;

import com.filerepository.userservice.model.AuditLog;
import com.filerepository.userservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public AuditLog saveAuditLog(AuditLog auditLog) {
        log.debug("Saving audit log: {}", auditLog);
        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> findByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    public List<AuditLog> findByResource(String resource) {
        return auditLogRepository.findByResource(resource);
    }

    public List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }

    public List<AuditLog> findByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }
}
