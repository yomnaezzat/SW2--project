package com.filerepository.fileservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "repository-service")
public interface RepositoryServiceClient {
    
    @GetMapping("/api/repositories/{id}/exists")
    boolean checkRepositoryExists(@PathVariable Long id);

    @GetMapping("/api/repositories/folders/{id}/exists")
    boolean checkFolderExists(@PathVariable Long id);
}
