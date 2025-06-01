package com.datn.motchill.dto.country;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for creating or updating Country
 */
@Data
public class CountryRequest {
    
    @NotBlank(message = "Tên quốc gia không được để trống")
    private String name;
    
    private String slug;
}
