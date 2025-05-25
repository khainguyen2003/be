package com.datn.motchill.admin.infrastructure.persistence.entity;

import com.datn.motchill.admin.presentation.enums.AdminMovieStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "TBL_MOVIES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminMoviesEntity extends AdminBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 2000, nullable = false)
    private String name;

    @Column(name = "TITLE", columnDefinition = "TEXT")
    private String title;

    @Column(name = "ORIGINAL_NAME", length = 2000)
    private String originalName;

    @Column(name = "SLUG", length = 100, nullable = false)
    private String slug;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "RELEASE_DATE")
    private Date releaseDate;

    @Column(name = "TIME")
    @Comment(value = "Thời lượng phim (phút)")
    private Integer time;

    @Column(name = "QUALITY")
    private String quality;

    @Column(name = "RATING_AVG")
    private BigDecimal ratingAvg;

    @Column(name = "THUMB_URL", length = 1024)
    private String thumbUrl;

    @Column(name = "POSTER_URL", length = 1024)
    private String posterUrl;

    @Column(name = "TRAILER_URL", length = 1024)
    private String trailerUrl;

    @Column(name = "VIEWS")
    private Long views;

    @Column(name = "SOURCES", length = 1024)
    private String souces;

    @Column(name = "GENRE", length = 255)
    private String genre;

    @Column(name = "COUNTRY", length = 255)
    private String country;

    @Column(name = "DIRECTOR", length = 255)
    private String director;

    @Column(name = "CASTS")
    private String casts;

    @Column(name = "TOTAL_EPISODES", precision = 10, scale = 0)
    private Integer totalEpisodes;

    @Column(name = "MOVIE_STATUS")
    @Convert(converter = AdminMovieStatusEnum.MovieStatusEnumConverter.class)
    private AdminMovieStatusEnum movieStatus;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "MODIFIED_AT")
    private Date modifiedAt;

    public void setStatusSaveDraftColected() {
        this.setStatusSaveDraft();
        this.setMovieStatus(AdminMovieStatusEnum.COLLECTED);
    }

    public boolean isCollected() {
        return this.movieStatus == AdminMovieStatusEnum.COLLECTED;
    }

    public boolean isPendingApproveCollected() {
        return this.movieStatus == AdminMovieStatusEnum.PENDING_APPROVE_COLLECTED;
    }

    public boolean isRejectedCollecting() {
        return this.movieStatus == AdminMovieStatusEnum.REJECTED_COLLECTING;
    }

    public boolean isCancelApproveCollecting() {
        return this.movieStatus == AdminMovieStatusEnum.CANCEL_APPROVE_COLLECTING;
    }

    public boolean isPendingTranslate() {
        return this.movieStatus == AdminMovieStatusEnum.PENDING_TRANSLATE;
    }

    public boolean isPendingApproveTranslate() {
        return this.movieStatus == AdminMovieStatusEnum.PENDING_APPROVE_TRANSLATE;
    }

    public boolean isTranslated() {
        return this.movieStatus == AdminMovieStatusEnum.TRANSLATED;
    }

    public boolean isRejectTranslate() {
        return this.movieStatus == AdminMovieStatusEnum.REJECT_TRANSLATE;
    }

    public boolean isCancelApproveTranslate() {
        return this.movieStatus == AdminMovieStatusEnum.CANCEL_APPROVE_TRANSLATE;
    }

    public boolean isPendingEdit() {
        return this.movieStatus == AdminMovieStatusEnum.PENDING_EDIT;
    }

    public boolean isEdited() {
        return this.movieStatus == AdminMovieStatusEnum.EDITED;
    }

    public boolean isRejectEdit() {
        return this.movieStatus == AdminMovieStatusEnum.REJECT_EDIT;
    }

    public boolean isCancelApproveEdit() {
        return this.movieStatus == AdminMovieStatusEnum.CANCEL_APPROVE_EDIT;
    }

    public boolean isApproved() {
        return this.movieStatus == AdminMovieStatusEnum.APPROVED;
    }

    public boolean isPublished() {
        return this.movieStatus == AdminMovieStatusEnum.PUBLISHED;
    }

    public boolean isUnpublished() {
        return this.movieStatus == AdminMovieStatusEnum.UNPUBLISHED;
    }

    public boolean isArchived() {
        return this.movieStatus == AdminMovieStatusEnum.ARCHIVED;
    }





}
