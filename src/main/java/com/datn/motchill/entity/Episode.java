package com.datn.motchill.entity;

import java.util.Date;

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
    @Column(name = "NAME", length = 2000, nullable = false)
    private String name;
    
    @Column(name = "TITLE", length = 2000)
    private String title;

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
    
    @Column(name = "VIDEO_URL", length = 1024)
    private String videoUrl;
    
    @JsonIgnore
    @Column(name = "VIDEO_PATH", length = 1024)
    private String videoPath;

    @Column(name = "HLS_FOLDER", length = 1024)
    private String hlsFolder;
    
    @Column(name = "STATUS", length = 50)
    private String status = "pending"; // pending, uploaded, processing, completed, error
    
    @Column(name = "PROCESSING_PROGRESS")
    private Double processingProgress;

    @Column(name = "SERVER_ID")
    private Long server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    private Movie movie;

    @Column(name = "THUMBNAIL_PATH", length = 1024)
    private String thumbnailPath;

    private Date createdDate;
    private Date updatedDate;
}
