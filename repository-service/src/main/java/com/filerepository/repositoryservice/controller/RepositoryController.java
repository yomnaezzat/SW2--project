package com.filerepository.repositoryservice.controller;

import com.filerepository.repositoryservice.dto.RepositoryRequest;
import com.filerepository.repositoryservice.dto.RepositoryResponse;
import com.filerepository.repositoryservice.service.RepositoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repositories")
@RequiredArgsConstructor
public class RepositoryController {

    private final RepositoryService repositoryService;

    @PostMapping
    public ResponseEntity<RepositoryResponse> createRepository(
            @Valid @RequestBody RepositoryRequest request,
            @RequestHeader("User-Id") Long userId) {
        RepositoryResponse response = repositoryService.createRepository(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{repositoryId}")
    public ResponseEntity<RepositoryResponse> getRepository(@PathVariable Long repositoryId) {
        RepositoryResponse response = repositoryService.getRepository(repositoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<RepositoryResponse>> getUserRepositories(
            @RequestHeader("User-Id") Long userId) {
        List<RepositoryResponse> responses = repositoryService.getUserRepositories(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{repositoryId}")
    public ResponseEntity<RepositoryResponse> updateRepository(
            @PathVariable Long repositoryId,
            @Valid @RequestBody RepositoryRequest request) {
        RepositoryResponse response = repositoryService.updateRepository(repositoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{repositoryId}")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long repositoryId) {
        repositoryService.deleteRepository(repositoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{repositoryId}/members/{userId}")
    public ResponseEntity<RepositoryResponse> addMember(
            @PathVariable Long repositoryId,
            @PathVariable Long userId) {
        RepositoryResponse response = repositoryService.addMember(repositoryId, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{repositoryId}/members/{userId}")
    public ResponseEntity<RepositoryResponse> removeMember(
            @PathVariable Long repositoryId,
            @PathVariable Long userId) {
        RepositoryResponse response = repositoryService.removeMember(repositoryId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{repositoryId}/milestones")
    public ResponseEntity<RepositoryResponse> addMilestone(
            @PathVariable Long repositoryId,
            @RequestParam String milestone) {
        RepositoryResponse response = repositoryService.addMilestone(repositoryId, milestone);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{repositoryId}/milestones")
    public ResponseEntity<RepositoryResponse> removeMilestone(
            @PathVariable Long repositoryId,
            @RequestParam String milestone) {
        RepositoryResponse response = repositoryService.removeMilestone(repositoryId, milestone);
        return ResponseEntity.ok(response);
    }
}