package com.datn.motchill.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "EPISODES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Episode  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 2000, nullable = false)
    private String name;

    @Column(name = "SLUG", length = 100, nullable = false)
    private String slug;

    @Column(name = "EPISODE_NUMBER")
    private Integer episodeNumber;

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

    @Column(name = "hls_folder")
    private String hlsFolder;

    @Column(name = "SERVER_ID")
    private Long server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    private Movie movie;

    private String videoPath; // Đường dẫn đến tệp .m3u8 hoặc .mp4
    private String thumbnailPath;
}
