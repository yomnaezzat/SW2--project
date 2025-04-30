package com.filerepository.repositoryservice.service;

import com.filerepository.repositoryservice.dto.RepositoryRequest;
import com.filerepository.repositoryservice.dto.RepositoryResponse;
import com.filerepository.repositoryservice.entity.Repository;
import com.filerepository.repositoryservice.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;

    @Transactional
    public RepositoryResponse createRepository(RepositoryRequest request, Long userId) {
        Repository repository = new Repository();
        repository.setName(request.getName());
        repository.setDescription(request.getDescription());
        repository.setCreatedBy(userId);

        if (request.getMemberIds() != null) {
            repository.setMemberIds(request.getMemberIds());
        }

        if (request.getMilestones() != null) {
            repository.setMilestones(request.getMilestones());
        }

        repository = repositoryRepository.save(repository);
        return mapToResponse(repository);
    }

    public RepositoryResponse getRepository(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));
        return mapToResponse(repository);
    }

    public List<RepositoryResponse> getUserRepositories(Long userId) {
        return repositoryRepository.findByUserAccess(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RepositoryResponse updateRepository(Long repositoryId, RepositoryRequest request) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        repository.setName(request.getName());
        repository.setDescription(request.getDescription());

        if (request.getMemberIds() != null) {
            repository.setMemberIds(request.getMemberIds());
        }

        if (request.getMilestones() != null) {
            repository.setMilestones(request.getMilestones());
        }

        repository = repositoryRepository.save(repository);
        return mapToResponse(repository);
    }

    @Transactional
    public void deleteRepository(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));
        repositoryRepository.delete(repository);
    }

    @Transactional
    public RepositoryResponse addMember(Long repositoryId, Long userId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        repository.getMemberIds().add(userId);
        repository = repositoryRepository.save(repository);
        return mapToResponse(repository);
    }

    @Transactional
    public RepositoryResponse removeMember(Long repositoryId, Long userId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        repository.getMemberIds().remove(userId);
        repository = repositoryRepository.save(repository);
        return mapToResponse(repository);
    }

    @Transactional
    public RepositoryResponse addMilestone(Long repositoryId, String milestone) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        repository.getMilestones().add(milestone);
        repository = repositoryRepository.save(repository);
        return mapToResponse(repository);
    }

    @Transactional
    public RepositoryResponse removeMilestone(Long repositoryId, String milestone) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        repository.getMilestones().remove(milestone);
        repository = repositoryRepository.save(repository);
        return mapToResponse(repository);
    }

    private RepositoryResponse mapToResponse(Repository repository) {
        RepositoryResponse response = new RepositoryResponse();
        response.setId(repository.getId());
        response.setName(repository.getName());
        response.setDescription(repository.getDescription());
        response.setCreatedBy(repository.getCreatedBy());
        response.setMemberIds(repository.getMemberIds());
        response.setMilestones(repository.getMilestones());
        response.setCreatedAt(repository.getCreatedAt());
        response.setUpdatedAt(repository.getUpdatedAt());
        return response;
    }
}