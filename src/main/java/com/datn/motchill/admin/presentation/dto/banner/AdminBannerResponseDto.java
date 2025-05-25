package com.datn.motchill.admin.presentation.dto.banner;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.BannerEntity}
 */
@Data
public class AdminBannerResponseDto implements Serializable {
    Long id;
    String slug;
    String name;
    String description;
    String logoUrl;
    String videoUrl;
}