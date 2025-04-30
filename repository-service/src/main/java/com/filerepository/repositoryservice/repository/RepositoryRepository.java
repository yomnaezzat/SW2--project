package com.filerepository.repositoryservice.repository;

import com.filerepository.repositoryservice.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    List<Repository> findByOwnerId(Long ownerId);
    
    @Query("SELECT r FROM Repository r JOIN r.supervisorIds s WHERE s = :supervisorId")
    List<Repository> findBySupervisorId(@Param("supervisorId") Long supervisorId);
    
    @Query("SELECT r FROM Repository r WHERE r.ownerId = :userId OR :userId MEMBER OF r.supervisorIds")
    List<Repository> findByUserIdAsOwnerOrSupervisor(@Param("userId") Long userId);
    
    boolean existsById(Long id);
    
    List<Repository> findByNameContainingIgnoreCase(String keyword);
}
