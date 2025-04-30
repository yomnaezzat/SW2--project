package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class RepositoryRequest {
    @NotBlank(message = "Repository name is required")
    private String name;

    private String description;

    private Set<Long> memberIds;

    private Set<String> milestones;
}