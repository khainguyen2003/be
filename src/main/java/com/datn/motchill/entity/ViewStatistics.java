package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "VIEW_STATISTICS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatistics extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // Số lượt xem trong ngày
    private Long dailyViews = 0L;

    // Số lượt xem trong tuần
    private Long weeklyViews = 0L;

    // Số lượt xem trong tháng
    private Long monthlyViews = 0L;

    // Số lượt xem trong năm
    private Long yearlyViews = 0L;

    // Tổng số lượt xem
    private Long totalViews = 0L;
}
