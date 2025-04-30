package com.filerepository.repositoryservice.repository;

import com.filerepository.repositoryservice.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByRepositoryId(Long repositoryId);
    List<Folder> findByRepositoryIdAndParentFolderId(Long repositoryId, Long parentFolderId);
    List<Folder> findByRepositoryIdAndIsMilestone(Long repositoryId, boolean isMilestone);
    Optional<Folder> findByRepositoryIdAndName(Long repositoryId, String name);
    boolean existsById(Long id);
}
