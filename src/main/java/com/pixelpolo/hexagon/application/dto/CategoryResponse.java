package com.pixelpolo.hexagon.application.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for category response.
 */
@Data
@Builder
public class CategoryResponse {

    private Long categoryId;
    private String name;
    private LocalDateTime deletionDate;

}
