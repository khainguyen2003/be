package com.datn.motchill.controller;

import com.datn.motchill.dto.viewstatistics.ViewStatisticsDto;
import com.datn.motchill.service.ViewStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class ViewStatisticsController {

    private final ViewStatisticsService viewStatisticsService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<ViewStatisticsDto> getStatisticsByMovieId(@PathVariable Long movieId) {
        return ResponseEntity.ok(viewStatisticsService.findByMovieId(movieId));
    }

    @GetMapping("/top-daily")
    public ResponseEntity<Page<ViewStatisticsDto>> getTopDaily(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(viewStatisticsService.getTopDailyViews(pageable));
    }

    @GetMapping("/top-weekly")
    public ResponseEntity<Page<ViewStatisticsDto>> getTopWeekly(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(viewStatisticsService.getTopWeeklyViews(pageable));
    }

    @GetMapping("/top-monthly")
    public ResponseEntity<Page<ViewStatisticsDto>> getTopMonthly(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(viewStatisticsService.getTopMonthlyViews(pageable));
    }

    @GetMapping("/top-yearly")
    public ResponseEntity<Page<ViewStatisticsDto>> getTopYearly(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(viewStatisticsService.getTopYearlyViews(pageable));
    }

    @GetMapping("/top-total")
    public ResponseEntity<Page<ViewStatisticsDto>> getTopTotal(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(viewStatisticsService.getTopTotalViews(pageable));
    }
}
