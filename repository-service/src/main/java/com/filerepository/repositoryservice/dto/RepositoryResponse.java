package com.filerepository.repositoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryResponse {
    private Long id;
    private String name;
    private String description;
    private Long createdBy;
    private Set<Long> memberIds;
    private Set<String> milestones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}