package com.filerepository.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileHistoryDTO {
    private Long id;
    private Long fileId;
    private String actionType;
    private String actionDescription;
    private Long userId;
    private String username;
    private LocalDateTime timestamp;
}
