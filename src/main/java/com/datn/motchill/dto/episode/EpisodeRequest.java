package com.datn.motchill.dto.episode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * Request DTO for creating or updating Episode
 */
@Data
public class EpisodeRequest {
    
    @NotNull(message = "ID phim không được để trống")
    private Long movieId;
    
    @NotNull(message = "Số tập không được để trống")
    @Min(value = 1, message = "Số tập phải lớn hơn 0")
    private Integer episodeNumber;
    
    private String title;
    
    private String description;
    
    private String videoUrl;
    
    private String thumbnail;
    
    @PositiveOrZero(message = "Thời lượng không được âm")
    private Integer duration;
}
