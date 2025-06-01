package com.datn.motchill.service;

import com.datn.motchill.dto.movie.MovieDto;
import com.datn.motchill.dto.movie.MovieFilterDTO;
import com.datn.motchill.dto.movie.MovieRequest;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.enums.MovieTypeEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    Page<MovieDto> searchAdmin(MovieFilterDTO filter);

    /**
     * Tìm phim theo ID
     */
    MovieDto findById(Long id);
    
    /**
     * Tìm phim theo slug
     */
    MovieDto findBySlug(String slug);

    MovieDto saveDraft(MovieRequest request);

    MovieDto updateDraft(Long id, MovieRequest request);

    /**
     * Xóa phim
     */
    void delete(Long id);
    
    /**
     * Thay đổi trạng thái phim
     */
    MovieDto updateStatus(Long id, MovieStatusEnum status);

    /**
     * Tìm phim theo thể loại
     */
    Page<MovieDto> findByGenreId(Long genreId, Pageable pageable);
    
    /**
     * Tìm phim theo quốc gia
     */
//    Page<MovieDto> findByCountryId(Long countryId, Pageable pageable);
    
    /**
     * Tìm phim theo tag
     */
//    Page<MovieDto> findByTagId(Long tagId, Pageable pageable);
    
    /**
     * Tìm phim đặc sắc
     */
    List<MovieDto> findFeatured(int limit);
    
    /**
     * Tìm phim mới cập nhật
     */
    List<MovieDto> findLatest(int limit);
    
    /**
     * Tìm phim có lượt xem cao nhất
     */
    List<MovieDto> findMostViewed(int limit);
    
    /**
     * Tìm kiếm phim theo từ khóa
     */
    Page<MovieDto> search(String keyword, Pageable pageable);

    /**
     * Find movies by type (phim lẻ, phim bộ)
     */
    Page<MovieDto> findByType(MovieTypeEnum type, Pageable pageable);
    
    /**
     * Find movies by status (hoàn thành, đang cập nhật)
     */
    Page<MovieDto> findByStatus(MovieStatusEnum status, Pageable pageable);
}
