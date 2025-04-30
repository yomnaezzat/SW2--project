package com.filerepository.fileservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadRequest {
    private MultipartFile file;
    private String description;
    private Long repositoryId;
    private String milestone;
    private String folder;
}