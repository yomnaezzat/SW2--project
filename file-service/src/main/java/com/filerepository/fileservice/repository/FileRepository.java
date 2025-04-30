package com.filerepository.fileservice.repository;

import com.filerepository.fileservice.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByUploaderId(Long uploaderId);
    List<FileEntity> findByRepositoryId(Long repositoryId);
    List<FileEntity> findByFolderId(Long folderId);
    List<FileEntity> findByRepositoryIdAndFolderId(Long repositoryId, Long folderId);
    List<FileEntity> findByFilenameContainingIgnoreCase(String keyword);
}
