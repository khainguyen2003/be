package com.datn.motchill.repository;

import com.datn.motchill.entity.ViewStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewStatisticsRepository extends JpaRepository<ViewStatistics, Long> {
    
    Optional<ViewStatistics> findByMovieId(Long movieId);
    
    @Query("SELECT vs FROM ViewStatistics vs ORDER BY vs.dailyViews DESC")
    Page<ViewStatistics> findAllOrderByDailyViewsDesc(Pageable pageable);
    
    @Query("SELECT vs FROM ViewStatistics vs ORDER BY vs.weeklyViews DESC")
    Page<ViewStatistics> findAllOrderByWeeklyViewsDesc(Pageable pageable);
    
    @Query("SELECT vs FROM ViewStatistics vs ORDER BY vs.monthlyViews DESC")
    Page<ViewStatistics> findAllOrderByMonthlyViewsDesc(Pageable pageable);
    
    @Query("SELECT vs FROM ViewStatistics vs ORDER BY vs.yearlyViews DESC")
    Page<ViewStatistics> findAllOrderByYearlyViewsDesc(Pageable pageable);
    
    @Query("SELECT vs FROM ViewStatistics vs ORDER BY vs.totalViews DESC")
    Page<ViewStatistics> findAllOrderByTotalViewsDesc(Pageable pageable);
    
    @Modifying
    @Query("UPDATE ViewStatistics vs SET vs.dailyViews = 0")
    void resetDailyViews();
    
    @Modifying
    @Query("UPDATE ViewStatistics vs SET vs.weeklyViews = 0")
    void resetWeeklyViews();
    
    @Modifying
    @Query("UPDATE ViewStatistics vs SET vs.monthlyViews = 0")
    void resetMonthlyViews();
    
    @Modifying
    @Query("UPDATE ViewStatistics vs SET vs.yearlyViews = 0")
    void resetYearlyViews();
}
