package com.datn.motchill.admin.presentation.dto.episodes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EpisodeCreateDTO {
    @NotNull(message = "Server không được để trống")
    private Long serverId;

    // Sinh tự động nếu không nhập
    private String name;

    private Integer episodeNumber;

    // Sinh tự động từ tên nếu không nhập
    private String slug;

    // đường dẫn để nhúng vào web của tập phim.
    @NotBlank(message = "Đường dẫn nhúng của tập phim không được để trống")
    private String embed;

    @NotBlank(message = "Đường dẫn m3u8 của tập phim không được để trống")
    private String m3u8;
}
