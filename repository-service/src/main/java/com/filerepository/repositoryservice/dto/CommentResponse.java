package com.filerepository.repositoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private Long fileId;
    private Long userId;
    private Long repositoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}