package com.datn.motchill.dto.viewhistory;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.datn.motchill.entity.ViewHistory}
 */
@Value
public class ViewHistoryDto implements Serializable {
    Long id;
    Long userId;
    String username;
    Long movieId;
    String movieTitle;
    String moviePoster;
    Long episodeId;
    String episodeTitle;
    Integer episodeNumber;
    Integer watchTime;
    Integer lastPosition;
    Boolean completed;
    LocalDateTime viewedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
