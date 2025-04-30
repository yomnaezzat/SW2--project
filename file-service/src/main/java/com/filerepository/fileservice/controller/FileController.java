package com.filerepository.fileservice.controller;

import com.filerepository.fileservice.dto.FileResponse;
import com.filerepository.fileservice.dto.FileUploadRequest;
import com.filerepository.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam Long repositoryId,
            @RequestParam(required = false) String milestone,
            @RequestParam(required = false) String folder,
            @RequestHeader("User-Id") Long userId) throws IOException {

        FileUploadRequest request = new FileUploadRequest();
        request.setFile(file);
        request.setDescription(description);
        request.setRepositoryId(repositoryId);
        request.setMilestone(milestone);
        request.setFolder(folder);

        FileResponse response = fileService.uploadFile(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) throws IOException {
        byte[] fileContent = fileService.downloadFile(fileId);
        FileResponse metadata = fileService.getFileMetadata(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(metadata.getFileType()))
                .body(fileContent);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFileMetadata(@PathVariable Long fileId) {
        FileResponse response = fileService.getFileMetadata(fileId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<List<FileResponse>> getFilesByRepository(@PathVariable Long repositoryId) {
        List<FileResponse> files = fileService.getFilesByRepository(repositoryId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/repository/{repositoryId}/milestone/{milestone}")
    public ResponseEntity<List<FileResponse>> getFilesByMilestone(
            @PathVariable Long repositoryId,
            @PathVariable String milestone) {
        List<FileResponse> files = fileService.getFilesByMilestone(repositoryId, milestone);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/repository/{repositoryId}/folder/{folder}")
    public ResponseEntity<List<FileResponse>> getFilesByFolder(
            @PathVariable Long repositoryId,
            @PathVariable String folder) {
        List<FileResponse> files = fileService.getFilesByFolder(repositoryId, folder);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}