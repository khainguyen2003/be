package com.datn.motchill.dto.movie;

import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.enums.MovieTypeEnum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

/**
 * Request DTO for creating or updating Movie
 */
@Data
public class MovieRequest {
    
    @NotBlank(message = "Tiêu đề phim không được để trống")
    private String name;

    private String originalName;
    
    private String slug;
    
    private String description;
    
    private String thumbnailUrl;
    
    private String trailerUrl;
    
    @NotNull(message = "Trạng thái phim không được để trống")
    private MovieStatusEnum status; // Hoàn thành, Đang cập nhật
    
    @NotNull(message = "Loại phim không được để trống")
    private MovieTypeEnum type; // Phim lẻ, Phim bộ
    
    @Min(value = 1900, message = "Năm phát hành phải từ 1900 trở lên")
    @Max(value = 2100, message = "Năm phát hành phải nhỏ hơn 2100")
    private Integer releaseYear;
    
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    private Integer duration; // Thời lượng phim (phút)
    
    private Integer quality; // 360p, 480p, 720p, 1080p
    
    private String language;
    
    private Set<Long> genreIds;
    
    private Set<Long> tagIds;
    
    private Set<Long> countryIds;
    
    private Set<Long> directorIds;
}
