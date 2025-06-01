package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "VIEW_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    private Episode episode;

    // Thời gian đã xem (giây)
    private Integer watchTime;

    // Vị trí dừng lại (giây)
    private Integer lastPosition;

    // Đã xem hoàn thành hay chưa
    private Boolean completed = false;
}
