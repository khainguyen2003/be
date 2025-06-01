package com.datn.motchill.service;

import com.datn.motchill.dto.viewhistory.ViewHistoryDto;
import com.datn.motchill.dto.viewhistory.ViewHistoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ViewHistoryService {
    
    /**
     * Tìm tất cả lịch sử xem của người dùng
     */
    Page<ViewHistoryDto> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Tìm lịch sử xem theo ID
     */
    ViewHistoryDto findById(Long id);
    
    /**
     * Tìm lịch sử xem phim theo user và movie
     */
    Optional<ViewHistoryDto> findByUserIdAndMovieId(Long userId, Long movieId);
    
    /**
     * Tìm lịch sử xem tập phim theo user, movie và episode
     */
    Optional<ViewHistoryDto> findByUserIdAndMovieIdAndEpisodeId(Long userId, Long movieId, Long episodeId);
    
    /**
     * Ghi nhận hoặc cập nhật lịch sử xem phim
     */
    ViewHistoryDto recordView(Long userId, ViewHistoryRequest request);
    
    /**
     * Xóa lịch sử xem
     */
    void delete(Long id);
    
    /**
     * Xóa tất cả lịch sử xem của người dùng
     */
    void deleteAllByUserId(Long userId);
}
