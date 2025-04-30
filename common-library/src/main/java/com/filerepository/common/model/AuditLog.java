package com.filerepository.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Model class for audit log entries
 * Used by audit aspect to store audit information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    private String action;
    private String resource;
    private String username;
    private String className;
    private String methodName;
    private String arguments;
    private LocalDateTime timestamp;
    private Long durationMs;
    private boolean success;
    private String errorMessage;
}
