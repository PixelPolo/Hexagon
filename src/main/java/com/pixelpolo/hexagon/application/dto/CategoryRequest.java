package com.pixelpolo.hexagon.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.pixelpolo.hexagon.application.validation.ValidationMessage;

/**
 * DTO for creating or updating a category.
 */
@Data
public class CategoryRequest {

    @NotNull(message = ValidationMessage.NOT_NULL)
    @NotBlank(message = ValidationMessage.NOT_BLANK)
    @Size(max = 128, message = ValidationMessage.MAX_SIZE_128)
    private String name;

}
