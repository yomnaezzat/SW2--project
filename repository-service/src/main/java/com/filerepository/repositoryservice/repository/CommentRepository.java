package com.filerepository.repositoryservice.repository;

import com.filerepository.repositoryservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFileId(Long fileId);
    List<Comment> findByRepositoryId(Long repositoryId);
    List<Comment> findByUserId(Long userId);
}