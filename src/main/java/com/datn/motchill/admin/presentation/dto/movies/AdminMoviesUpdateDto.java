package com.datn.motchill.admin.presentation.dto.movies;

import com.datn.motchill.admin.common.utils.Constants;
import com.datn.motchill.admin.presentation.dto.episodes.EpisodeCreateDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Author: khainv_llq<br/>
 * Since: 04/26/2025 07:44 AM<br/>
 * Description: 
 */
@Data
public class AdminMoviesUpdateDto implements Serializable {
    private Long id;
    @NotBlank(message = "Tên phim không được bỏ trống")
    @Size(max = 2000, message = "Tên phim không được vượt quá 2000 ký tự")
    private String name;

    @NotBlank(message = "Tên gốc của phim không được bỏ trống")
    @Size(max = 2000, message = "Tên gốc của phim không được vượt quá 2000 ký tự")
    private String originalName;

    @NotBlank(message = "Tiêu đề phim không được bỏ trống")
    private String title;

    @Size(max = 255, message = "Đạo diễn của phim không được vượt quá 255 ký tự")
    private String director;

    @NotBlank(message = "Mô tả phim không được bỏ trống")
    private String description;

    @DateTimeFormat(pattern = Constants.DateFormatType.DD_MM_YYYY_HH_MM_SS2)
    @JsonFormat(pattern = Constants.DateFormatType.DD_MM_YYYY_HH_MM_SS2)
    private LocalDateTime releaseDate;

    @NotEmpty(message = "Danh sách tập không được để trống")
    private List<EpisodeCreateDTO> episodes;

    @DecimalMin(value = "0.0", inclusive = false, message = "Thời lượng phim (Phút) phải lớn hơn 0")
    private Float durationMinutes;

    @Size(max = 1024, message = "Link poster phim không được vượt quá 1024 ký tự")
    private String poster;

    @Size(max = 1024, message = "Link thumb phim không được vượt quá 1024 ký tự")
    private String thumb;

    @Size(max = 1024, message = "Link trailer không được vượt quá 1024 ký tự")
    private String trailerUrl;

    @Size(max = 1024, message = "Link nguồn phim không được vượt quá 1024 ký tự")
    private String souces;
}