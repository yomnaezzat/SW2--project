package com.filerepository.repositoryservice.service;

import com.filerepository.common.annotation.Audited;
import com.filerepository.common.annotation.LogExecutionTime;
import com.filerepository.repositoryservice.dto.FolderRequest;
import com.filerepository.repositoryservice.exception.ResourceNotFoundException;
import com.filerepository.repositoryservice.model.Folder;
import com.filerepository.repositoryservice.repository.FolderRepository;
import com.filerepository.repositoryservice.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderService {

    private final FolderRepository folderRepository;
    private final RepositoryRepository repositoryRepository;

    @Audited(action = "CREATE_FOLDER", resource = "FOLDER")
    @LogExecutionTime
    @Transactional
    public Folder createFolder(FolderRequest request) {
        // Verify repository exists
        if (!repositoryRepository.existsById(request.getRepositoryId())) {
            throw new ResourceNotFoundException("Repository not found with id: " + request.getRepositoryId());
        }
        
        // If parent folder is specified, verify it exists
        if (request.getParentFolderId() != null && 
            !folderRepository.existsById(request.getParentFolderId())) {
            throw new ResourceNotFoundException("Parent folder not found with id: " + request.getParentFolderId());
        }
        
        Folder folder = Folder.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isMilestone(request.isMilestone())
                .repositoryId(request.getRepositoryId())
                .parentFolderId(request.getParentFolderId())
                .build();
        
        return folderRepository.save(folder);
    }

    @Audited(action = "GET_FOLDER", resource = "FOLDER")
    public Folder getFolderById(Long folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + folderId));
    }

    @Audited(action = "UPDATE_FOLDER", resource = "FOLDER")
    @Transactional
    public Folder updateFolder(Long folderId, FolderRequest request) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + folderId));
        
        folder.setName(request.getName());
        folder.setDescription(request.getDescription());
        folder.setMilestone(request.isMilestone());
        
        // If parent folder is updated, verify it exists
        if (request.getParentFolderId() != null && 
            !folderRepository.existsById(request.getParentFolderId())) {
            throw new ResourceNotFoundException("Parent folder not found with id: " + request.getParentFolderId());
        }
        
        folder.setParentFolderId(request.getParentFolderId());
        
        return folderRepository.save(folder);
    }

    @Audited(action = "DELETE_FOLDER", resource = "FOLDER")
    @Transactional
    public void deleteFolder(Long folderId) {
        if (!folderRepository.existsById(folderId)) {
            throw new ResourceNotFoundException("Folder not found with id: " + folderId);
        }
        
        // NOTE: In a real-world scenario, you might want to handle the case where
        // the folder has child folders or contains files. Here, we're simply
        // deleting the folder without any additional checks.
        
        folderRepository.deleteById(folderId);
    }

    @Audited(action = "GET_REPOSITORY_FOLDERS", resource = "FOLDER")
    public List<Folder> getFoldersByRepositoryId(Long repositoryId) {
        return folderRepository.findByRepositoryId(repositoryId);
    }

    @Audited(action = "GET_SUBFOLDER", resource = "FOLDER")
    public List<Folder> getSubfolders(Long repositoryId, Long parentFolderId) {
        return folderRepository.findByRepositoryIdAndParentFolderId(repositoryId, parentFolderId);
    }

    @Audited(action = "GET_MILESTONES", resource = "FOLDER")
    public List<Folder> getMilestones(Long repositoryId) {
        return folderRepository.findByRepositoryIdAndIsMilestone(repositoryId, true);
    }

    @Audited(action = "CHECK_FOLDER_EXISTS", resource = "FOLDER")
    public boolean checkFolderExists(Long folderId) {
        return folderRepository.existsById(folderId);
    }
}
