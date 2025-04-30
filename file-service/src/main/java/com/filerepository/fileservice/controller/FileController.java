package com.filerepository.fileservice.controller;

import com.filerepository.fileservice.dto.FileUploadResponse;
import com.filerepository.fileservice.model.FileEntity;
import com.filerepository.fileservice.model.FileHistory;
import com.filerepository.fileservice.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Management", description = "APIs for file operations")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    
    private final FileService fileService;
    
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload a file", description = "Uploads a file to the repository")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("repositoryId") Long repositoryId,
            @RequestParam(value = "folderId", required = false) Long folderId,
            @RequestParam("uploaderId") Long uploaderId,
            @RequestParam(value = "description", required = false) String description) {
        
        log.info("Received file upload request: filename={}, repositoryId={}, folderId={}, uploaderId={}",
                file.getOriginalFilename(), repositoryId, folderId, uploaderId);
        
        FileUploadResponse response = fileService.uploadFile(file, repositoryId, folderId, uploaderId, description);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "Download a file", description = "Downloads a file by its ID")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileId,
            @RequestParam("userId") Long userId) {
        
        log.info("Received file download request: fileId={}, userId={}", fileId, userId);
        
        // Get file details
        FileEntity fileEntity = fileService.getFileById(fileId);
        Resource resource = fileService.downloadFile(fileId, userId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getOriginalFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/{fileId}")
    @Operation(summary = "Update a file", description = "Updates a file with new content or description")
    public ResponseEntity<FileUploadResponse> updateFile(
            @PathVariable Long fileId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("userId") Long userId) {
        
        log.info("Received file update request: fileId={}, userId={}", fileId, userId);
        
        FileUploadResponse response = fileService.updateFile(fileId, file, description, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete a file", description = "Deletes a file by its ID")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long fileId,
            @RequestParam("userId") Long userId) {
        
        log.info("Received file delete request: fileId={}, userId={}", fileId, userId);
        
        fileService.deleteFile(fileId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Get file details", description = "Retrieves file details by ID")
    public ResponseEntity<FileEntity> getFileById(@PathVariable Long fileId) {
        FileEntity fileEntity = fileService.getFileById(fileId);
        return ResponseEntity.ok(fileEntity);
    }

    @GetMapping("/repository/{repositoryId}")
    @Operation(summary = "Get files by repository", description = "Retrieves all files in a repository")
    public ResponseEntity<List<FileEntity>> getFilesByRepositoryId(@PathVariable Long repositoryId) {
        List<FileEntity> files = fileService.getFilesByRepositoryId(repositoryId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/folder/{folderId}")
    @Operation(summary = "Get files by folder", description = "Retrieves all files in a folder")
    public ResponseEntity<List<FileEntity>> getFilesByFolderId(@PathVariable Long folderId) {
        List<FileEntity> files = fileService.getFilesByFolderId(folderId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/uploader/{uploaderId}")
    @Operation(summary = "Get files by uploader", description = "Retrieves all files uploaded by a user")
    public ResponseEntity<List<FileEntity>> getFilesByUploaderId(@PathVariable Long uploaderId) {
        List<FileEntity> files = fileService.getFilesByUploaderId(uploaderId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/search")
    @Operation(summary = "Search files", description = "Searches for files by keyword")
    public ResponseEntity<List<FileEntity>> searchFiles(@RequestParam("keyword") String keyword) {
        List<FileEntity> files = fileService.searchFiles(keyword);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}/history")
    @Operation(summary = "Get file history", description = "Retrieves the history of operations on a file")
    public ResponseEntity<List<FileHistory>> getFileHistory(@PathVariable Long fileId) {
        List<FileHistory> history = fileService.getFileHistory(fileId);
        return ResponseEntity.ok(history);
    }
}