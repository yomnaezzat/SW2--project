package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryRequest {
    @NotBlank(message = "Repository name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Owner ID is required")
    private Long ownerId;
    
    private Set<Long> supervisorIds;
}
