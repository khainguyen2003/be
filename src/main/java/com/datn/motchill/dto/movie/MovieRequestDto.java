package com.datn.motchill.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MovieRequestDto {
    @NotBlank(message = "Tên phim không được để trống")
    @Size(max = 255, message = "Tên phim tối đa 255 ký tự")
    private String name;

    @NotBlank(message = "Slug không được để trống")
    @Size(max = 255, message = "Slug tối đa 255 ký tự")
    private String slug;

    @Size(max = 255, message = "Tên gốc tối đa 255 ký tự")
    private String originalName;

    @Size(max = 512, message = "URL thumbnail tối đa 512 ký tự")
    private String thumbUrl;

    @Size(max = 512, message = "URL poster tối đa 512 ký tự")
    private String posterUrl;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @Size(max = 50, message = "Thời lượng tối đa 50 ký tự")
    private String time;

    @Size(max = 20, message = "Chất lượng tối đa 20 ký tự")
    private String quality;

    @Size(max = 100, message = "Ngôn ngữ tối đa 100 ký tự")
    private String language;

    @NotEmpty(message = "Danh sách diễn viên không được để trống")
    private List<Long> castIds;

    @NotEmpty(message = "Danh sách đạo diễn không được để trống")
    private List<Long> directorIds;

    @NotEmpty(message = "Danh sách thể loại không được để trống")
    private List<Long> genreIds;

    @NotNull(message = "Quốc gia không được để trống")
    private Long countryId;
}
