package com.filerepository.fileservice.service;

import com.filerepository.fileservice.dto.FileResponse;
import com.filerepository.fileservice.dto.FileUploadRequest;
import com.filerepository.fileservice.entity.File;
import com.filerepository.fileservice.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public FileResponse uploadFile(FileUploadRequest request, Long userId) throws IOException {
        MultipartFile multipartFile = request.getFile();
        String fileName = generateUniqueFileName(multipartFile.getOriginalFilename());
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        // Create directories if they don't exist
        Files.createDirectories(targetLocation.getParent());

        // Copy file to the target location
        Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Create file entity
        File file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setFilePath(targetLocation.toString());
        file.setFileType(multipartFile.getContentType());
        file.setDescription(request.getDescription());
        file.setUploadedBy(userId);
        file.setRepositoryId(request.getRepositoryId());
        file.setMilestone(request.getMilestone());
        file.setFolder(request.getFolder());
        file.setFileSize(multipartFile.getSize());

        file = fileRepository.save(file);

        return mapToFileResponse(file);
    }

    public byte[] downloadFile(Long fileId) throws IOException {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path filePath = Paths.get(file.getFilePath());
        return Files.readAllBytes(filePath);
    }

    public FileResponse getFileMetadata(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return mapToFileResponse(file);
    }

    public List<FileResponse> getFilesByRepository(Long repositoryId) {
        return fileRepository.findByRepositoryId(repositoryId)
                .stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
    }

    public List<FileResponse> getFilesByMilestone(Long repositoryId, String milestone) {
        return fileRepository.findByRepositoryIdAndMilestone(repositoryId, milestone)
                .stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
    }

    public List<FileResponse> getFilesByFolder(Long repositoryId, String folder) {
        return fileRepository.findByRepositoryIdAndFolder(repositoryId, folder)
                .stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFile(Long fileId) throws IOException {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path filePath = Paths.get(file.getFilePath());
        Files.deleteIfExists(filePath);

        fileRepository.delete(file);
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
            originalFileName = originalFileName.substring(0, dotIndex);
        }
        return originalFileName + "_" + UUID.randomUUID().toString() + extension;
    }

    private FileResponse mapToFileResponse(File file) {
        FileResponse response = new FileResponse();
        response.setId(file.getId());
        response.setFileName(file.getFileName());
        response.setFileType(file.getFileType());
        response.setDescription(file.getDescription());
        response.setUploadedBy(file.getUploadedBy());
        response.setRepositoryId(file.getRepositoryId());
        response.setMilestone(file.getMilestone());
        response.setFolder(file.getFolder());
        response.setFileSize(file.getFileSize());
        response.setCreatedAt(file.getCreatedAt());
        response.setUpdatedAt(file.getUpdatedAt());

        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/download/")
                .path(file.getId().toString())
                .toUriString();
        response.setDownloadUrl(downloadUrl);

        return response;
    }
}