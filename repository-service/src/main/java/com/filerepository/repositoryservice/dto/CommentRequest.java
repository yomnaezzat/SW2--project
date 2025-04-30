package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Comment content is required")
    private String content;
    
    @NotNull(message = "File ID is required")
    private Long fileId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private boolean isSupervisorComment;
}
