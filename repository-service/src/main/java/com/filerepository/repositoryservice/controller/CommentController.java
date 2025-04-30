package com.filerepository.repositoryservice.controller;

import com.filerepository.repositoryservice.dto.CommentRequest;
import com.filerepository.repositoryservice.dto.CommentResponse;
import com.filerepository.repositoryservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repositories/{repositoryId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long repositoryId,
            @Valid @RequestBody CommentRequest request,
            @RequestHeader("User-Id") Long userId) {
        CommentResponse response = commentService.createComment(request, userId, repositoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByFile(@PathVariable Long fileId) {
        List<CommentResponse> responses = commentService.getCommentsByFile(fileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByRepository(@PathVariable Long repositoryId) {
        List<CommentResponse> responses = commentService.getCommentsByRepository(repositoryId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody String content) {
        CommentResponse response = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}