//package com.datn.motchill.controller;
//
//import com.datn.motchill.dto.movie.MovieDto;
//import com.datn.motchill.dto.movie.MovieFilterDTO;
//import com.datn.motchill.enums.MovieStatusEnum;
//import com.datn.motchill.enums.MovieTypeEnum;
//import com.datn.motchill.service.MovieService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Controller for public access to movies
// */
//@RestController
//@RequestMapping("/api/movies")
//@RequiredArgsConstructor
//public class PublicMovieController {
//
//    private final MovieService movieService;
//
//    /**
//     * Get all movies with pagination
//     */
//    @GetMapping
//    public ResponseEntity<Page<MovieDto>> getAllMovies(Pageable pageable) {
//        return ResponseEntity.ok(movieService.findAll(pageable));
//    }
//
//    /**
//     * Get movies by filter with pagination
//     */
//    @PostMapping("/filter")
//    public ResponseEntity<Page<MovieDto>> getMoviesByFilter(@RequestBody MovieFilterDTO filter, Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByFilter(filter, pageable));
//    }
//
//    /**
//     * Get a movie by id
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
//        MovieDto movie = movieService.findById(id);
//        // Tăng lượt xem mỗi khi người dùng xem chi tiết phim
//        movieService.incrementViewCount(id);
//        return ResponseEntity.ok(movie);
//    }
//
//    /**
//     * Get a movie by slug
//     */
//    @GetMapping("/slug/{slug}")
//    public ResponseEntity<MovieDto> getMovieBySlug(@PathVariable String slug) {
//        MovieDto movie = movieService.findBySlug(slug);
//        // Tăng lượt xem mỗi khi người dùng xem chi tiết phim
//        movieService.incrementViewCount(movie.getId());
//        return ResponseEntity.ok(movie);
//    }
//
//    /**
//     * Search movies by keyword
//     */
//    @GetMapping("/search")
//    public ResponseEntity<Page<MovieDto>> searchMovies(
//            @RequestParam String keyword,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.searchByKeyword(keyword, pageable));
//    }
//
//    /**
//     * Get latest movies
//     */
//    @GetMapping("/latest")
//    public ResponseEntity<Page<MovieDto>> getLatestMovies(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        return ResponseEntity.ok(movieService.findLatestMovies(pageable));
//    }
//
//    /**
//     * Get top movies by view count
//     */
//    @GetMapping("/top-views")
//    public ResponseEntity<Page<MovieDto>> getTopMoviesByViewCount(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok(movieService.findTopByViewCount(pageable));
//    }
//
//    /**
//     * Get top movies by rating
//     */
//    @GetMapping("/top-rated")
//    public ResponseEntity<Page<MovieDto>> getTopMoviesByRating(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok(movieService.findTopByRating(pageable));
//    }
//
//    /**
//     * Get movies by genre
//     */
//    @GetMapping("/genre/{genreId}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByGenre(
//            @PathVariable Long genreId,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByGenreId(genreId, pageable));
//    }
//
//    /**
//     * Get movies by country
//     */
//    @GetMapping("/country/{countryId}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByCountry(
//            @PathVariable Long countryId,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByCountryId(countryId, pageable));
//    }
//
//    /**
//     * Get movies by director
//     */
//    @GetMapping("/director/{directorId}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByDirector(
//            @PathVariable Long directorId,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByDirectorId(directorId, pageable));
//    }
//
//    /**
//     * Get movies by tag
//     */
//    @GetMapping("/tag/{tagId}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByTag(
//            @PathVariable Long tagId,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByTagId(tagId, pageable));
//    }
//
//    /**
//     * Get movies by release year
//     */
//    @GetMapping("/year/{year}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByYear(
//            @PathVariable Integer year,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByReleaseYear(year, pageable));
//    }
//
//    /**
//     * Get movies by type (phim lẻ, phim bộ)
//     */
//    @GetMapping("/type/{type}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByType(
//            @PathVariable MovieTypeEnum type,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByType(type, pageable));
//    }
//
//    /**
//     * Get movies by status (hoàn thành, đang cập nhật)
//     */
//    @GetMapping("/status/{status}")
//    public ResponseEntity<Page<MovieDto>> getMoviesByStatus(
//            @PathVariable MovieStatusEnum status,
//            Pageable pageable) {
//        return ResponseEntity.ok(movieService.findByStatus(status, pageable));
//    }
//}
