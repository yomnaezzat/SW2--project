package com.filerepository.repositoryservice.service;

import com.filerepository.common.annotation.Audited;
import com.filerepository.common.annotation.LogExecutionTime;
import com.filerepository.common.dto.UserDTO;
import com.filerepository.repositoryservice.client.UserServiceClient;
import com.filerepository.repositoryservice.dto.RepositoryRequest;
import com.filerepository.repositoryservice.dto.RepositoryResponse;
import com.filerepository.repositoryservice.dto.SupervisorDTO;
import com.filerepository.repositoryservice.exception.ResourceNotFoundException;
import com.filerepository.repositoryservice.model.Repository;
import com.filerepository.repositoryservice.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;
    private final UserServiceClient userServiceClient;

    @Audited(action = "CREATE_REPOSITORY", resource = "REPOSITORY")
    @LogExecutionTime
    @Transactional
    public RepositoryResponse createRepository(RepositoryRequest request) {
        Repository repository = Repository.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ownerId(request.getOwnerId())
                .supervisorIds(request.getSupervisorIds() != null ? request.getSupervisorIds() : new HashSet<>())
                .build();
        
        Repository savedRepository = repositoryRepository.save(repository);
        return convertToResponse(savedRepository);
    }

    @Audited(action = "GET_REPOSITORY", resource = "REPOSITORY")
    public RepositoryResponse getRepositoryById(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Repository not found with id: " + repositoryId));
        
        return convertToResponse(repository);
    }

    @Audited(action = "UPDATE_REPOSITORY", resource = "REPOSITORY")
    @Transactional
    public RepositoryResponse updateRepository(Long repositoryId, RepositoryRequest request) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Repository not found with id: " + repositoryId));
        
        repository.setName(request.getName());
        repository.setDescription(request.getDescription());
        
        if (request.getSupervisorIds() != null) {
            repository.setSupervisorIds(request.getSupervisorIds());
        }
        
        Repository updatedRepository = repositoryRepository.save(repository);
        return convertToResponse(updatedRepository);
    }

    @Audited(action = "DELETE_REPOSITORY", resource = "REPOSITORY")
    @Transactional
    public void deleteRepository(Long repositoryId) {
        if (!repositoryRepository.existsById(repositoryId)) {
            throw new ResourceNotFoundException("Repository not found with id: " + repositoryId);
        }
        
        repositoryRepository.deleteById(repositoryId);
    }

    @Audited(action = "GET_USER_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> getRepositoriesByOwnerId(Long ownerId) {
        List<Repository> repositories = repositoryRepository.findByOwnerId(ownerId);
        return repositories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Audited(action = "GET_SUPERVISOR_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> getRepositoriesBySupervisorId(Long supervisorId) {
        List<Repository> repositories = repositoryRepository.findBySupervisorId(supervisorId);
        return repositories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Audited(action = "GET_USER_ALL_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> getRepositoriesByUserIdAsOwnerOrSupervisor(Long userId) {
        List<Repository> repositories = repositoryRepository.findByUserIdAsOwnerOrSupervisor(userId);
        return repositories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Audited(action = "SEARCH_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> searchRepositories(String keyword) {
        List<Repository> repositories = repositoryRepository.findByNameContainingIgnoreCase(keyword);
        return repositories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Audited(action = "CHECK_REPOSITORY_EXISTS", resource = "REPOSITORY")
    public boolean checkRepositoryExists(Long repositoryId) {
        return repositoryRepository.existsById(repositoryId);
    }

    private RepositoryResponse convertToResponse(Repository repository) {
        // Get owner details
        UserDTO owner = userServiceClient.getUserById(repository.getOwnerId());
        
        // Get supervisor details
        Set<SupervisorDTO> supervisors = new HashSet<>();
        if (repository.getSupervisorIds() != null && !repository.getSupervisorIds().isEmpty()) {
            for (Long supervisorId : repository.getSupervisorIds()) {
                try {
                    UserDTO user = userServiceClient.getUserById(supervisorId);
                    supervisors.add(SupervisorDTO.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .fullName(user.getFullName())
                            .build());
                } catch (Exception e) {
                    log.error("Could not retrieve supervisor details for id: {}", supervisorId, e);
                }
            }
        }
        
        return RepositoryResponse.builder()
                .id(repository.getId())
                .name(repository.getName())
                .description(repository.getDescription())
                .ownerId(repository.getOwnerId())
                .ownerName(owner.getFullName())
                .supervisors(supervisors)
                .createdAt(repository.getCreatedAt())
                .updatedAt(repository.getUpdatedAt())
                .build();
    }
}
