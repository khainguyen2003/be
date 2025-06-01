package com.datn.motchill.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for creating or updating Tag
 */
@Data
public class TagRequest {
    @NotBlank(message = "Tên thẻ không được để trống")
    @Size(max = 100, message = "Tên thẻ không được vượt quá 100 ký tự")
    private String name;
    
    @Size(max = 100, message = "Slug không được vượt quá 100 ký tự")
    private String slug;
}
