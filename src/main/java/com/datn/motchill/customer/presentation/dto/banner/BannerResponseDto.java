package com.datn.motchill.customer.presentation.dto.banner;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.BannerEntity}
 */
@Data
public class BannerResponseDto implements Serializable {
    Long id;
    String slug;
    String name;
    String description;
    String logoUrl;
    String videoUrl;
}