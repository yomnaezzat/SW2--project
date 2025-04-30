package com.filerepository.fileservice.repository;

import com.filerepository.fileservice.model.FileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileHistoryRepository extends JpaRepository<FileHistory, Long> {
    List<FileHistory> findByFileId(Long fileId);
    List<FileHistory> findByUserId(Long userId);
    List<FileHistory> findByFileIdOrderByTimestampDesc(Long fileId);
}
