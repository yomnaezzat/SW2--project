package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Comment content is required")
    private String content;

    @NotNull(message = "File ID is required")
    private Long fileId;
}