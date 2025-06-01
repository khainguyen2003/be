package com.datn.motchill.dto.viewstatistics;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.datn.motchill.entity.ViewStatistics}
 */
@Value
public class ViewStatisticsDto implements Serializable {
    Long id;
    Long movieId;
    String movieTitle;
    String moviePoster;
    Integer dailyViews;
    Integer weeklyViews;
    Integer monthlyViews;
    Integer yearlyViews;
    Integer totalViews;
    LocalDateTime lastUpdated;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
