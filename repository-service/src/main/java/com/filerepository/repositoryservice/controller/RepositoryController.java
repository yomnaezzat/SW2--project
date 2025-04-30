package com.filerepository.repositoryservice.controller;

import com.filerepository.repositoryservice.dto.RepositoryRequest;
import com.filerepository.repositoryservice.dto.RepositoryResponse;
import com.filerepository.repositoryservice.service.RepositoryService;
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
@RequestMapping("/api/repositories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Repository Management", description = "APIs for repository operations")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @PostMapping
    @Operation(summary = "Create a repository", description = "Creates a new project repository")
    public ResponseEntity<RepositoryResponse> createRepository(@Valid @RequestBody RepositoryRequest request) {
        log.info("Creating repository: {}", request.getName());
        RepositoryResponse response = repositoryService.createRepository(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get repository", description = "Retrieves repository details by ID")
    public ResponseEntity<RepositoryResponse> getRepositoryById(@PathVariable("id") Long repositoryId) {
        log.info("Fetching repository with id: {}", repositoryId);
        RepositoryResponse response = repositoryService.getRepositoryById(repositoryId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update repository", description = "Updates an existing repository")
    public ResponseEntity<RepositoryResponse> updateRepository(
            @PathVariable("id") Long repositoryId,
            @Valid @RequestBody RepositoryRequest request) {
        log.info("Updating repository with id: {}", repositoryId);
        RepositoryResponse response = repositoryService.updateRepository(repositoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete repository", description = "Deletes a repository by ID")
    public ResponseEntity<Void> deleteRepository(@PathVariable("id") Long repositoryId) {
        log.info("Deleting repository with id: {}", repositoryId);
        repositoryService.deleteRepository(repositoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get owner repositories", description = "Retrieves all repositories owned by a user")
    public ResponseEntity<List<RepositoryResponse>> getRepositoriesByOwnerId(@PathVariable Long ownerId) {
        log.info("Fetching repositories for owner with id: {}", ownerId);
        List<RepositoryResponse> repositories = repositoryService.getRepositoriesByOwnerId(ownerId);
        return ResponseEntity.ok(repositories);
    }

    @GetMapping("/supervisor/{supervisorId}")
    @Operation(summary = "Get supervisor repositories", description = "Retrieves all repositories where user is a supervisor")
    public ResponseEntity<List<RepositoryResponse>> getRepositoriesBySupervisorId(@PathVariable Long supervisorId) {
        log.info("Fetching repositories for supervisor with id: {}", supervisorId);
        List<RepositoryResponse> repositories = repositoryService.getRepositoriesBySupervisorId(supervisorId);
        return ResponseEntity.ok(repositories);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user repositories", description = "Retrieves all repositories where user is owner or supervisor")
    public ResponseEntity<List<RepositoryResponse>> getRepositoriesByUserIdAsOwnerOrSupervisor(@PathVariable Long userId) {
        log.info("Fetching all repositories for user with id: {}", userId);
        List<RepositoryResponse> repositories = repositoryService.getRepositoriesByUserIdAsOwnerOrSupervisor(userId);
        return ResponseEntity.ok(repositories);
    }

    @GetMapping("/search")
    @Operation(summary = "Search repositories", description = "Searches repositories by name")
    public ResponseEntity<List<RepositoryResponse>> searchRepositories(@RequestParam("keyword") String keyword) {
        log.info("Searching repositories with keyword: {}", keyword);
        List<RepositoryResponse> repositories = repositoryService.searchRepositories(keyword);
        return ResponseEntity.ok(repositories);
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check repository exists", description = "Checks if a repository exists by ID")
    public ResponseEntity<Boolean> checkRepositoryExists(@PathVariable("id") Long repositoryId) {
        boolean exists = repositoryService.checkRepositoryExists(repositoryId);
        return ResponseEntity.ok(exists);
    }
}
