package com.datn.motchill.admin.presentation.dto.movies;

import com.datn.motchill.admin.presentation.enums.MovieTypeEnum;
import com.datn.motchill.shared.annotation.ColumnMapping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AdminMovieResponseDTO {
    @ColumnMapping("ID")
    private Long id;

    @ColumnMapping("NAME")
    private String name;

    @ColumnMapping("ORIGINAL_NAME")
    private String originalName;

    @ColumnMapping("SLUG")
    private String slug;

    @ColumnMapping("DIRECTOR")
    private String director;

    @ColumnMapping("DESCRIPTION")
    private String description;

    @ColumnMapping("RELEASE_DATE")
    private Date releaseDate;

    @ColumnMapping("TIME")
    private Integer time;

    @ColumnMapping("QUALITY")
    private String quality;

    @ColumnMapping("RATING_AVG")
    private BigDecimal ratingAvg;

    @ColumnMapping("THUMB_URL")
    private String thumbUrl;

    @ColumnMapping("POSTER_URL")
    private String posterUrl;

    @ColumnMapping("TRAILER_URL")
    private String trailerUrl;

    @ColumnMapping("VIEWS")
    private Long views;

    @ColumnMapping("SOURCES")
    private String souces;

    @ColumnMapping("MOVIES")
    private Boolean isSeries;

    private MovieTypeEnum movieType;

    @ColumnMapping("CASTS")
    private String casts;

    @ColumnMapping("CREATED_AT")
    private Date createdAt;
}
