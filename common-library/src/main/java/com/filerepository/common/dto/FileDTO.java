package com.filerepository.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO for transferring file information between services
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
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
    private LocalDateTime updatedAt;
}
