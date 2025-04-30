package com.filerepository.repositoryservice.service;

import com.filerepository.repositoryservice.dto.CommentRequest;
import com.filerepository.repositoryservice.dto.CommentResponse;
import com.filerepository.repositoryservice.entity.Comment;
import com.filerepository.repositoryservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse createComment(CommentRequest request, Long userId, Long repositoryId) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setFileId(request.getFileId());
        comment.setUserId(userId);
        comment.setRepositoryId(repositoryId);

        comment = commentRepository.save(comment);
        return mapToResponse(comment);
    }

    public List<CommentResponse> getCommentsByFile(Long fileId) {
        return commentRepository.findByFileId(fileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getCommentsByRepository(Long repositoryId) {
        return commentRepository.findByRepositoryId(repositoryId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(content);
        comment = commentRepository.save(comment);
        return mapToResponse(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setFileId(comment.getFileId());
        response.setUserId(comment.getUserId());
        response.setRepositoryId(comment.getRepositoryId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }
}