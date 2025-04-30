package com.filerepository.repositoryservice.controller;

import com.filerepository.repositoryservice.dto.CommentRequest;
import com.filerepository.repositoryservice.model.Comment;
import com.filerepository.repositoryservice.service.CommentService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comment Management", description = "APIs for comment operations")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create a comment", description = "Creates a new comment on a file")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentRequest request) {
        log.info("Creating comment for file: {} by user: {}", request.getFileId(), request.getUserId());
        Comment comment = commentService.createComment(request);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get comment", description = "Retrieves comment details by ID")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") Long commentId) {
        log.info("Fetching comment with id: {}", commentId);
        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update comment", description = "Updates an existing comment")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("id") Long commentId,
            @RequestParam String content) {
        log.info("Updating comment with id: {}", commentId);
        Comment comment = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment", description = "Deletes a comment by ID")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long commentId) {
        log.info("Deleting comment with id: {}", commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/file/{fileId}")
    @Operation(summary = "Get file comments", description = "Retrieves all comments on a file")
    public ResponseEntity<List<Comment>> getCommentsByFileId(@PathVariable Long fileId) {
        log.info("Fetching comments for file with id: {}", fileId);
        List<Comment> comments = commentService.getCommentsByFileId(fileId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user comments", description = "Retrieves all comments made by a user")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
        log.info("Fetching comments for user with id: {}", userId);
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/file/{fileId}/supervisor")
    @Operation(summary = "Get supervisor comments", description = "Retrieves all supervisor comments on a file")
    public ResponseEntity<List<Comment>> getSupervisorComments(@PathVariable Long fileId) {
        log.info("Fetching supervisor comments for file with id: {}", fileId);
        List<Comment> comments = commentService.getSupervisorComments(fileId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/file/{fileId}/student")
    @Operation(summary = "Get student comments", description = "Retrieves all student comments on a file")
    public ResponseEntity<List<Comment>> getStudentComments(@PathVariable Long fileId) {
        log.info("Fetching student comments for file with id: {}", fileId);
        List<Comment> comments = commentService.getStudentComments(fileId);
        return ResponseEntity.ok(comments);
    }
}
