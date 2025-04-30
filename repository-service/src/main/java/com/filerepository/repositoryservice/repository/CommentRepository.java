package com.filerepository.repositoryservice.repository;

import com.filerepository.repositoryservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFileId(Long fileId);
    List<Comment> findByFileIdOrderByCreatedAtDesc(Long fileId);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByIsSupervisorComment(boolean isSupervisorComment);
    List<Comment> findByFileIdAndIsSupervisorComment(Long fileId, boolean isSupervisorComment);
}
