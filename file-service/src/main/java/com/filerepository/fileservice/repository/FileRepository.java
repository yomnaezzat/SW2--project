package com.filerepository.fileservice.repository;

import com.filerepository.fileservice.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByRepositoryId(Long repositoryId);
    List<File> findByUploadedBy(Long uploadedBy);
    List<File> findByRepositoryIdAndMilestone(Long repositoryId, String milestone);
    List<File> findByRepositoryIdAndFolder(Long repositoryId, String folder);
}