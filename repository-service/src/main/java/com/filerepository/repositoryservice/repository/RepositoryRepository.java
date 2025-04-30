package com.filerepository.repositoryservice.repository;

import com.filerepository.repositoryservice.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    List<Repository> findByCreatedBy(Long createdBy);

    @Query("SELECT r FROM Repository r JOIN r.memberIds m WHERE m = :userId")
    List<Repository> findByMemberUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Repository r WHERE r.createdBy = :userId OR :userId IN (SELECT m FROM r.memberIds m)")
    List<Repository> findByUserAccess(@Param("userId") Long userId);
}