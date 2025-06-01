package com.datn.motchill.dto.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for creating or updating Genre
 */
@Data
public class GenreRequest {
    
    @NotBlank(message = "Tên thể loại không được để trống")
    private String name;
    
    private String slug;
}
