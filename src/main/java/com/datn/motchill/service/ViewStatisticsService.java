package com.datn.motchill.service;

import com.datn.motchill.dto.viewstatistics.ViewStatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ViewStatisticsService {
    
    /**
     * Tìm thống kê lượt xem theo ID phim
     */
    ViewStatisticsDto findByMovieId(Long movieId);
    
    /**
     * Tăng lượt xem cho phim
     */
    void incrementViews(Long movieId);
    
    /**
     * Lấy danh sách phim có lượt xem cao nhất trong ngày
     */
    Page<ViewStatisticsDto> getTopDailyViews(Pageable pageable);
    
    /**
     * Lấy danh sách phim có lượt xem cao nhất trong tuần
     */
    Page<ViewStatisticsDto> getTopWeeklyViews(Pageable pageable);
    
    /**
     * Lấy danh sách phim có lượt xem cao nhất trong tháng
     */
    Page<ViewStatisticsDto> getTopMonthlyViews(Pageable pageable);
    
    /**
     * Lấy danh sách phim có lượt xem cao nhất trong năm
     */
    Page<ViewStatisticsDto> getTopYearlyViews(Pageable pageable);
    
    /**
     * Lấy danh sách phim có tổng lượt xem cao nhất
     */
    Page<ViewStatisticsDto> getTopTotalViews(Pageable pageable);
    
    /**
     * Cập nhật lại thống kê theo khoảng thời gian (chạy định kỳ)
     * Reset daily views hàng ngày, weekly views hàng tuần, monthly views hàng tháng, yearly views hàng năm
     */
    void updatePeriodicStatistics();
}
