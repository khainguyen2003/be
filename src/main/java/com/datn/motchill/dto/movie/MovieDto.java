package com.datn.motchill.dto.movie;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.datn.motchill.dto.country.CountryDto;
import com.datn.motchill.dto.genre.GenreDto;
import com.datn.motchill.dto.tag.TagDto;
import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.enums.MovieTypeEnum;

import jakarta.persistence.Convert;
import lombok.Data;

/**
 * DTO for {@link com.datn.motchill.entity.Movie}
 */
@Data
public class MovieDto implements Serializable {
    Long id;
    private String name;
    private String slug;
    private String originalName;
    private String thumbUrl;
    private String posterUrl;
    private String description;
    String poster;
    String backdrop;
    String trailer;
    Integer releaseYear;
    LocalDate releaseDate;
    String duration;

    @Convert(converter = MovieTypeEnum.MovieTypeEnumConverter.class)
    MovieTypeEnum mediaType;
    
    @Convert(converter = MovieStatusEnum.MovieStatusEnumConverter.class)
    MovieStatusEnum status;
    boolean featured;
    Double imdbRating;
    String imdbId;
    String tmdbId;
    String director;
    String cast;
    Integer episodeCount;
    String quality;
    String ageRating;
    List<CountryDto> countries;
    List<GenreDto> genres;
    List<TagDto> tags;
    Integer totalViews;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
