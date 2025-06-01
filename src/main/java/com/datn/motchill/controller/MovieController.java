package com.datn.motchill.controller;

import com.datn.motchill.dto.movie.MovieDto;
import com.datn.motchill.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for public movie API endpoints
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /**
     * Lấy thông tin phim theo id
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findById(id));
    }

    /**
     * Lấy thông tin phim theo slug
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<MovieDto> getMovieBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(movieService.findBySlug(slug));
    }

    /**
     * Tìm kiếm phim theo từ khóa
     */
    @GetMapping("/search")
    public ResponseEntity<Page<MovieDto>> searchMovies(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(movieService.search(keyword, pageable));
    }

    /**
     * Lấy danh sách phim theo thể loại
     */
    @GetMapping("/genre/{genreId}")
    public ResponseEntity<Page<MovieDto>> getMoviesByGenre(@PathVariable Long genreId, Pageable pageable) {
        return ResponseEntity.ok(movieService.findByGenreId(genreId, pageable));
    }

    /**
     * Lấy danh sách phim theo quốc gia
     */
//    @GetMapping("/country/{countryId}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByCountry(@PathVariable Long countryId, Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByCountryId(countryId, pageable));
//    }
//
//    /**
//     * Lấy danh sách phim theo tag
//     */
//    @GetMapping("/tag/{tagId}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByTag(@PathVariable Long tagId, Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByTagId(tagId, pageable));
//    }

    /**
     * Lấy danh sách phim đặc sắc
     */
    @GetMapping("/featured")
    public ResponseEntity<List<MovieDto>> getFeaturedMovies(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(movieService.findFeatured(limit));
    }

    /**
     * Lấy danh sách phim mới cập nhật
     */
    @GetMapping("/latest")
    public ResponseEntity<List<MovieDto>> getLatestMovies(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(movieService.findLatest(limit));
    }

    /**
     * Lấy danh sách phim xem nhiều nhất
     */
    @GetMapping("/most-viewed")
    public ResponseEntity<List<MovieDto>> getMostViewedMovies(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(movieService.findMostViewed(limit));
    }
}
