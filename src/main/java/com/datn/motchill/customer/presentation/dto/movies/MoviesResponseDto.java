package com.datn.motchill.customer.presentation.dto.movies;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author: khainv_llq<br/>
 * Since: 04/26/2025 07:44 AM<br/>
 * Description: 
 */
@Data
public class MoviesResponseDto implements Serializable {
    private Long movieId;

    private String name;

    private String originalName;

    private String slug;

    private String director;

    private String description;

    private Date releaseDate;

    private Integer episodeNumber;

    private Integer time;

    private String quality;

    private BigDecimal ratingAvg;

    private String thumbUrl;

    private String posterUrl;

    private String trailerUrl;

    private Long views;

    private String souces;

    private Boolean isSeries;

    private String casts;

    private LocalDateTime createdAt;
}