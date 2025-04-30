package com.filerepository.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponse {
    private Long id;
    private String filename;
    private String originalFilename;
    private String contentType;
    private String description;
    private Long size;
    private Long repositoryId;
    private Long folderId;
    private Long uploaderId;
    private String uploaderName;
    private LocalDateTime uploadedAt;
}
