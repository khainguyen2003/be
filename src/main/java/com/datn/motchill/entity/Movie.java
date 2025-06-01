package com.datn.motchill.entity;

import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.enums.MovieTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MOVIE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends BaseEntity {

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, unique = true, nullable = false)
    private String slug;

    @Column(name = "original_name", length = 255)
    private String originalName;

    @Column(name = "thumb_url", length = 512)
    private String thumbUrl;

    @Column(name = "poster_url", length = 512)
    private String posterUrl;

    @Lob
    private String description;

    @Column(name = "total_episodes")
    private int totalEpisodes;

    @Column(name = "current_episode", length = 50)
    private String currentEpisode;

    @Column(length = 50)
    private String time;

    @Column(length = 20)
    private String quality;

    @Column(length = 100)
    private String language;

    @Convert(converter = MovieStatusEnum.MovieStatusEnumConverter.class)
    private MovieStatusEnum status;

    @Convert(converter = MovieTypeEnum.MovieTypeEnumConverter.class)
    private MovieTypeEnum movieType;

    @ManyToMany
    @JoinTable(
            name = "movie_cast",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "cast_id")
    )
    private List<Actor> casts = new ArrayList<>();

    // Many-to-Many với đạo diễn
    @ManyToMany
    @JoinTable(
            name = "movie_director",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private List<Director> directors = new ArrayList<>();

    // Many-to-Many với thể loại (genre)
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Episode> episodes = new ArrayList<>();
}
