package com.datn.motchill.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "TBL_EPISODES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminEpisodesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 2000, nullable = false)
    private String name;

    @Column(name = "SLUG", length = 100, nullable = false)
    private String slug;

    @Column(name = "RELEASE_DATE")
    private Date releaseDate;

    @Column(name = "DURATION_MINUTES")
    @Comment(value = "Thời lượng phim (phút)")
    private Integer durationMinutes;

    @Column(name = "RATING_AVG", precision = 2, scale = 1)
    private BigDecimal ratingAvg;

    @Column(name = "POSTER_URL")
    private String posterUrl;

    @Column(name = "THUMB_URL")
    private String thumbUrl;

    @Column(name = "TRAILER_URL", length = 500)
    private String trailerUrl;

    @Column(name = "EPISODE_URLSUB", length = 400)
    private String urlSub;

    @Column(name = "VIEWS")
    private Long views;

    @Column(name = "SOURCES", length = 4000)
    private String sources;

    @Column(name = "EMBED", length = 1024)
    private String embed;

    @Column(name = "M3U8", length = 1024)
    private String m3u8;

    @Column(name = "MOVIE_ID")
    private Long movieId;

    @Column(name = "SERVER_ID")
    private Long server;

    @Column(name = "EPISODE_LANG")
    private String episodeLang;
}
