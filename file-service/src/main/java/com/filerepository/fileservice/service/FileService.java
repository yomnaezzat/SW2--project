package com.filerepository.fileservice.service;

import com.filerepository.common.annotation.Audited;
import com.filerepository.common.annotation.LogExecutionTime;
import com.filerepository.common.dto.UserDTO;
import com.filerepository.fileservice.client.RepositoryServiceClient;
import com.filerepository.fileservice.client.UserServiceClient;
import com.filerepository.fileservice.dto.FileUploadResponse;
import com.filerepository.fileservice.exception.FileNotFoundException;
import com.filerepository.fileservice.model.FileEntity;
import com.filerepository.fileservice.model.FileHistory;
import com.filerepository.fileservice.repository.FileHistoryRepository;
import com.filerepository.fileservice.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final FileHistoryRepository fileHistoryRepository;
    private final FileStorageService fileStorageService;
    private final UserServiceClient userServiceClient;
    private final RepositoryServiceClient repositoryServiceClient;

    @Audited(action = "UPLOAD_FILE", resource = "FILE")
    @LogExecutionTime
    @Transactional
    public FileUploadResponse uploadFile(MultipartFile file, Long repositoryId, Long folderId, Long uploaderId, String description) {
        // Store the file physically
        String storedFilename = fileStorageService.storeFile(file);
        
        // Get user details from user service
        UserDTO uploader = userServiceClient.getUserById(uploaderId);
        
        // Create the file entity
        FileEntity fileEntity = FileEntity.builder()
                .filename(storedFilename)
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .description(description)
                .size(file.getSize())
                .storagePath(storedFilename)
                .repositoryId(repositoryId)
                .folderId(folderId)
                .uploaderId(uploaderId)
                .uploaderName(uploader.getFullName())
                .build();
        
        // Save to database
        FileEntity savedFile = fileRepository.save(fileEntity);
        
        // Create file history entry
        FileHistory fileHistory = FileHistory.builder()
                .fileId(savedFile.getId())
                .actionType("UPLOAD")
                .actionDescription("File uploaded")
                .userId(uploaderId)
                .username(uploader.getUsername())
                .build();
        
        fileHistoryRepository.save(fileHistory);
        
        return FileUploadResponse.builder()
                .id(savedFile.getId())
                .filename(savedFile.getFilename())
                .originalFilename(savedFile.getOriginalFilename())
                .contentType(savedFile.getContentType())
                .description(savedFile.getDescription())
                .size(savedFile.getSize())
                .repositoryId(savedFile.getRepositoryId())
                .folderId(savedFile.getFolderId())
                .uploaderId(savedFile.getUploaderId())
                .uploaderName(savedFile.getUploaderName())
                .uploadedAt(savedFile.getUploadedAt())
                .build();
    }

    @Audited(action = "DOWNLOAD_FILE", resource = "FILE")
    @LogExecutionTime
    public Resource downloadFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
        
        // Get user details from user service
        UserDTO user = userServiceClient.getUserById(userId);
        
        // Create file history entry
        FileHistory fileHistory = FileHistory.builder()
                .fileId(fileId)
                .actionType("DOWNLOAD")
                .actionDescription("File downloaded")
                .userId(userId)
                .username(user.getUsername())
                .build();
        
        fileHistoryRepository.save(fileHistory);
        
        // Return the file resource
        return fileStorageService.loadFileAsResource(fileEntity.getStoragePath());
    }

    @Audited(action = "UPDATE_FILE", resource = "FILE")
    @LogExecutionTime
    @Transactional
    public FileUploadResponse updateFile(Long fileId, MultipartFile file, String description, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
        
        // If a new file is provided, store it and update the path
        if (file != null && !file.isEmpty()) {
            // Delete the old file
            fileStorageService.deleteFile(fileEntity.getStoragePath());
            
            // Store the new file
            String storedFilename = fileStorageService.storeFile(file);
            
            fileEntity.setFilename(storedFilename);
            fileEntity.setOriginalFilename(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());
            fileEntity.setStoragePath(storedFilename);
        }
        
        // Update description if provided
        if (description != null) {
            fileEntity.setDescription(description);
        }
        
        // Get user details from user service
        UserDTO user = userServiceClient.getUserById(userId);
        
        // Create file history entry
        FileHistory fileHistory = FileHistory.builder()
                .fileId(fileId)
                .actionType("UPDATE")
                .actionDescription("File updated")
                .userId(userId)
                .username(user.getUsername())
                .build();
        
        fileHistoryRepository.save(fileHistory);
        
        // Save updated file
        FileEntity updatedFile = fileRepository.save(fileEntity);
        
        return FileUploadResponse.builder()
                .id(updatedFile.getId())
                .filename(updatedFile.getFilename())
                .originalFilename(updatedFile.getOriginalFilename())
                .contentType(updatedFile.getContentType())
                .description(updatedFile.getDescription())
                .size(updatedFile.getSize())
                .repositoryId(updatedFile.getRepositoryId())
                .folderId(updatedFile.getFolderId())
                .uploaderId(updatedFile.getUploaderId())
                .uploaderName(updatedFile.getUploaderName())
                .uploadedAt(updatedFile.getUploadedAt())
                .build();
    }

    @Audited(action = "DELETE_FILE", resource = "FILE")
    @LogExecutionTime
    @Transactional
    public void deleteFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
        
        // Delete the physical file
        fileStorageService.deleteFile(fileEntity.getStoragePath());
        
        // Get user details from user service
        UserDTO user = userServiceClient.getUserById(userId);
        
        // Create file history entry
        FileHistory fileHistory = FileHistory.builder()
                .fileId(fileId)
                .actionType("DELETE")
                .actionDescription("File deleted")
                .userId(userId)
                .username(user.getUsername())
                .build();
        
        fileHistoryRepository.save(fileHistory);
        
        // Delete the database record
        fileRepository.delete(fileEntity);
    }

    @Audited(action = "GET_FILE", resource = "FILE")
    public FileEntity getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }

    @Audited(action = "GET_FILES_BY_REPOSITORY", resource = "FILE")
    public List<FileEntity> getFilesByRepositoryId(Long repositoryId) {
        return fileRepository.findByRepositoryId(repositoryId);
    }

    @Audited(action = "GET_FILES_BY_FOLDER", resource = "FILE")
    public List<FileEntity> getFilesByFolderId(Long folderId) {
        return fileRepository.findByFolderId(folderId);
    }

    @Audited(action = "GET_FILES_BY_REPOSITORY_AND_FOLDER", resource = "FILE")
    public List<FileEntity> getFilesByRepositoryIdAndFolderId(Long repositoryId, Long folderId) {
        return fileRepository.findByRepositoryIdAndFolderId(repositoryId, folderId);
    }

    @Audited(action = "GET_FILES_BY_UPLOADER", resource = "FILE")
    public List<FileEntity> getFilesByUploaderId(Long uploaderId) {
        return fileRepository.findByUploaderId(uploaderId);
    }

    @Audited(action = "SEARCH_FILES", resource = "FILE")
    public List<FileEntity> searchFiles(String keyword) {
        return fileRepository.findByFilenameContainingIgnoreCase(keyword);
    }

    @Audited(action = "GET_FILE_HISTORY", resource = "FILE")
    public List<FileHistory> getFileHistory(Long fileId) {
        return fileHistoryRepository.findByFileIdOrderByTimestampDesc(fileId);
    }
}
