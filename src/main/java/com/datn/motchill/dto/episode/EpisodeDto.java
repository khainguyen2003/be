package com.datn.motchill.dto.episode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDto implements Serializable {
    private Long id;
    private String title;
    private int episodeNumber;
    private Long movieId;
    private String status; // pending, uploaded, processing, completed, error
    private String videoUrl;
    private String embed;
    private String m3u8;
    private String slug;
    private Double processingProgress;
    private String name; // Giữ lại để tương thích ngược

    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
