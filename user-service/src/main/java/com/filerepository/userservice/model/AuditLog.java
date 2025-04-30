package com.filerepository.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String action;
    
    @Column(nullable = false)
    private String resource;
    
    @Column(nullable = false)
    private String username;
    
    @Column(name = "class_name")
    private String className;
    
    @Column(name = "method_name")
    private String methodName;
    
    @Column(length = 1000)
    private String arguments;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "duration_ms")
    private Long durationMs;
    
    @Column(nullable = false)
    private boolean success;
    
    @Column(name = "error_message", length = 1000)
    private String errorMessage;
}
