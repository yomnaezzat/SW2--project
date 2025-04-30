package com.filerepository.repositoryservice.controller;

import com.filerepository.repositoryservice.dto.FolderRequest;
import com.filerepository.repositoryservice.model.Folder;
import com.filerepository.repositoryservice.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repositories/folders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Folder Management", description = "APIs for folder operations")
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    @Operation(summary = "Create a folder", description = "Creates a new folder in a repository")
    public ResponseEntity<Folder> createFolder(@Valid @RequestBody FolderRequest request) {
        log.info("Creating folder: {} in repository: {}", request.getName(), request.getRepositoryId());
        Folder folder = folderService.createFolder(request);
        return new ResponseEntity<>(folder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get folder", description = "Retrieves folder details by ID")
    public ResponseEntity<Folder> getFolderById(@PathVariable("id") Long folderId) {
        log.info("Fetching folder with id: {}", folderId);
        Folder folder = folderService.getFolderById(folderId);
        return ResponseEntity.ok(folder);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update folder", description = "Updates an existing folder")
    public ResponseEntity<Folder> updateFolder(
            @PathVariable("id") Long folderId,
            @Valid @RequestBody FolderRequest request) {
        log.info("Updating folder with id: {}", folderId);
        Folder folder = folderService.updateFolder(folderId, request);
        return ResponseEntity.ok(folder);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete folder", description = "Deletes a folder by ID")
    public ResponseEntity<Void> deleteFolder(@PathVariable("id") Long folderId) {
        log.info("Deleting folder with id: {}", folderId);
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/repository/{repositoryId}")
    @Operation(summary = "Get repository folders", description = "Retrieves all folders in a repository")
    public ResponseEntity<List<Folder>> getFoldersByRepositoryId(@PathVariable Long repositoryId) {
        log.info("Fetching folders for repository with id: {}", repositoryId);
        List<Folder> folders = folderService.getFoldersByRepositoryId(repositoryId);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/repository/{repositoryId}/subfolders/{parentFolderId}")
    @Operation(summary = "Get subfolders", description = "Retrieves all subfolders of a parent folder")
    public ResponseEntity<List<Folder>> getSubfolders(
            @PathVariable Long repositoryId,
            @PathVariable Long parentFolderId) {
        log.info("Fetching subfolders for repository: {} and parent folder: {}", repositoryId, parentFolderId);
        List<Folder> folders = folderService.getSubfolders(repositoryId, parentFolderId);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/repository/{repositoryId}/milestones")
    @Operation(summary = "Get milestones", description = "Retrieves all milestone folders in a repository")
    public ResponseEntity<List<Folder>> getMilestones(@PathVariable Long repositoryId) {
        log.info("Fetching milestones for repository with id: {}", repositoryId);
        List<Folder> milestones = folderService.getMilestones(repositoryId);
        return ResponseEntity.ok(milestones);
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check folder exists", description = "Checks if a folder exists by ID")
    public ResponseEntity<Boolean> checkFolderExists(@PathVariable("id") Long folderId) {
        boolean exists = folderService.checkFolderExists(folderId);
        return ResponseEntity.ok(exists);
    }
}
