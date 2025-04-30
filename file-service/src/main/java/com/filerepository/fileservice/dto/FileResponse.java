package com.filerepository.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String description;
    private Long uploadedBy;
    private Long repositoryId;
    private String milestone;
    private String folder;
    private Long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String downloadUrl;
}