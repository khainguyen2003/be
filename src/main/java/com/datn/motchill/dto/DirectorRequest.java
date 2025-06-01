package com.datn.motchill.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for creating or updating Director
 */
@Data
public class DirectorRequest {
    
    @NotBlank(message = "Tên đạo diễn không được để trống")
    private String name;
    
    private String slug;
    
    private String biography;
    
    private String avatarUrl;
}
