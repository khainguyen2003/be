package com.datn.motchill.service.impl;

import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.dto.viewstatistics.ViewStatisticsDto;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.entity.ViewStatistics;
import com.datn.motchill.repository.MovieRepository;
import com.datn.motchill.repository.ViewStatisticsRepository;
import com.datn.motchill.service.ViewStatisticsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ViewStatisticsServiceImpl implements ViewStatisticsService {

    private final ViewStatisticsRepository viewStatisticsRepository;
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public ViewStatisticsDto findByMovieId(Long movieId) {
        return viewStatisticsRepository.findByMovieId(movieId)
                .map(item -> modelMapper.map(item, ViewStatisticsDto.class))
                .orElseThrow(() -> new NotFoundException("View statistics not found for movie: " + movieId));
    }

    @Override
    public void incrementViews(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + movieId));
        
        ViewStatistics statistics = viewStatisticsRepository.findByMovieId(movieId)
                .orElse(new ViewStatistics());
        
        if (statistics.getId() == null) {
            statistics.setMovie(movie);
            statistics.setDailyViews(0L);
            statistics.setWeeklyViews(0L);
            statistics.setMonthlyViews(0L);
            statistics.setYearlyViews(0L);
            statistics.setTotalViews(0L);
        }
        
        statistics.setDailyViews(statistics.getDailyViews() + 1);
        statistics.setWeeklyViews(statistics.getWeeklyViews() + 1);
        statistics.setMonthlyViews(statistics.getMonthlyViews() + 1);
        statistics.setYearlyViews(statistics.getYearlyViews() + 1);
        statistics.setTotalViews(statistics.getTotalViews() + 1);
        
        viewStatisticsRepository.save(statistics);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViewStatisticsDto> getTopDailyViews(Pageable pageable) {
        return viewStatisticsRepository.findAllOrderByDailyViewsDesc(pageable)
                .map(item -> modelMapper.map(item, ViewStatisticsDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViewStatisticsDto> getTopWeeklyViews(Pageable pageable) {
        return viewStatisticsRepository.findAllOrderByWeeklyViewsDesc(pageable)
                .map(item -> modelMapper.map(item, ViewStatisticsDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViewStatisticsDto> getTopMonthlyViews(Pageable pageable) {
        return viewStatisticsRepository.findAllOrderByMonthlyViewsDesc(pageable)
                .map(item -> modelMapper.map(item, ViewStatisticsDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViewStatisticsDto> getTopYearlyViews(Pageable pageable) {
        return viewStatisticsRepository.findAllOrderByYearlyViewsDesc(pageable)
                .map(item -> modelMapper.map(item, ViewStatisticsDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViewStatisticsDto> getTopTotalViews(Pageable pageable) {
        return viewStatisticsRepository.findAllOrderByTotalViewsDesc(pageable)
                .map(item -> modelMapper.map(item, ViewStatisticsDto.class));
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Chạy vào lúc 0h hàng ngày
    public void updatePeriodicStatistics() {
        LocalDateTime now = LocalDateTime.now();
        
        // Xử lý reset dailyViews hàng ngày
        resetDailyViews();
        
        // Xử lý reset weeklyViews vào thứ Hai hàng tuần
        if (now.getDayOfWeek() == DayOfWeek.MONDAY) {
            resetWeeklyViews();
        }
        
        // Xử lý reset monthlyViews vào ngày đầu tiên của tháng
        if (now.getDayOfMonth() == 1) {
            resetMonthlyViews();
        }
        
        // Xử lý reset yearlyViews vào ngày đầu tiên của năm
        if (now.getDayOfYear() == 1) {
            resetYearlyViews();
        }
    }
    
    private void resetDailyViews() {
        viewStatisticsRepository.resetDailyViews();
    }
    
    private void resetWeeklyViews() {
        viewStatisticsRepository.resetWeeklyViews();
    }
    
    private void resetMonthlyViews() {
        viewStatisticsRepository.resetMonthlyViews();
    }
    
    private void resetYearlyViews() {
        viewStatisticsRepository.resetYearlyViews();
    }
}
