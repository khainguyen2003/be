package com.datn.motchill.dto.movie;

import com.datn.motchill.dto.ActorDto;
import com.datn.motchill.dto.CountryDto;
import com.datn.motchill.dto.DirectorDto;
import com.datn.motchill.dto.EpisodeDto;
import com.datn.motchill.dto.genre.GenreDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieResponseDto {
    Long id;
    String name;
    String slug;
    String originalName;
    String thumbUrl;
    String posterUrl;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime created;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime modified;
    String description;
    int totalEpisodes;
    String currentEpisode;
    String time;
    String quality;
    String language;

    // Danh sách diễn viên, chỉ lấy id + name
    List<ActorDto> casts;

    // Danh sách đạo diễn
    List<DirectorDto> directors;

    // Danh sách thể loại
    List<GenreDto> genres;

    // Quốc gia
    CountryDto country;

    List<EpisodeDto> episodes;
}
