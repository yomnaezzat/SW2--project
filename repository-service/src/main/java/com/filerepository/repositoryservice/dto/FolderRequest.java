package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderRequest {
    @NotBlank(message = "Folder name is required")
    private String name;
    
    private String description;
    
    private boolean isMilestone;
    
    @NotNull(message = "Repository ID is required")
    private Long repositoryId;
    
    private Long parentFolderId;
}
