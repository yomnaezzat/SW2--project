package com.filerepository.repositoryservice.service;

import com.filerepository.common.annotation.Audited;
import com.filerepository.common.annotation.LogExecutionTime;
import com.filerepository.common.dto.UserDTO;
import com.filerepository.repositoryservice.client.UserServiceClient;
import com.filerepository.repositoryservice.dto.CommentRequest;
import com.filerepository.repositoryservice.exception.ResourceNotFoundException;
import com.filerepository.repositoryservice.model.Comment;
import com.filerepository.repositoryservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserServiceClient userServiceClient;

    @Audited(action = "CREATE_COMMENT", resource = "COMMENT")
    @LogExecutionTime
    @Transactional
    public Comment createComment(CommentRequest request) {
        // Get user details
        UserDTO user = userServiceClient.getUserById(request.getUserId());
        
        Comment comment = Comment.builder()
                .content(request.getContent())
                .fileId(request.getFileId())
                .userId(request.getUserId())
                .username(user.getUsername())
                .isSupervisorComment(request.isSupervisorComment())
                .build();
        
        return commentRepository.save(comment);
    }

    @Audited(action = "GET_COMMENT", resource = "COMMENT")
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
    }

    @Audited(action = "UPDATE_COMMENT", resource = "COMMENT")
    @Transactional
    public Comment updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        comment.setContent(content);
        
        return commentRepository.save(comment);
    }

    @Audited(action = "DELETE_COMMENT", resource = "COMMENT")
    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId);
        }
        
        commentRepository.deleteById(commentId);
    }

    @Audited(action = "GET_FILE_COMMENTS", resource = "COMMENT")
    public List<Comment> getCommentsByFileId(Long fileId) {
        return commentRepository.findByFileIdOrderByCreatedAtDesc(fileId);
    }

    @Audited(action = "GET_USER_COMMENTS", resource = "COMMENT")
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Audited(action = "GET_SUPERVISOR_COMMENTS", resource = "COMMENT")
    public List<Comment> getSupervisorComments(Long fileId) {
        return commentRepository.findByFileIdAndIsSupervisorComment(fileId, true);
    }

    @Audited(action = "GET_STUDENT_COMMENTS", resource = "COMMENT")
    public List<Comment> getStudentComments(Long fileId) {
        return commentRepository.findByFileIdAndIsSupervisorComment(fileId, false);
    }
}
