//package com.datn.motchill.controller;
//
//import com.datn.motchill.dto.episode.EpisodeDto;
//import com.datn.motchill.service.EpisodeService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * Controller for public episode API endpoints
// */
//@RestController
//@RequestMapping("/api/episodes")
//@RequiredArgsConstructor
//public class PublicEpisodeController {
//
//    private final EpisodeService episodeService;
//
//    /**
//     * Lấy danh sách tập phim theo phim với phân trang
//     */
//    @GetMapping("/movie/{movieId}")
//    public ResponseEntity<Page<EpisodeDto>> getAllEpisodesByMovieId(
//            @PathVariable Long movieId,
//            Pageable pageable) {
//        return ResponseEntity.ok(episodeService.findAllByMovieId(movieId, pageable));
//    }
//
//    /**
//     * Lấy danh sách tất cả tập phim của một phim
//     */
//    @GetMapping("/movie/{movieId}/all")
//    public ResponseEntity<List<EpisodeDto>> getAllEpisodesByMovieId(@PathVariable Long movieId) {
//        return ResponseEntity.ok(episodeService.findAllByMovieId(movieId));
//    }
//
//    /**
//     * Lấy thông tin tập phim theo id
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<EpisodeDto> getEpisodeById(@PathVariable Long id) {
//        return ResponseEntity.ok(episodeService.findById(id));
//    }
//
//    /**
//     * Lấy thông tin tập phim theo phim và số tập
//     */
//    @GetMapping("/movie/{movieId}/number/{episodeNumber}")
//    public ResponseEntity<EpisodeDto> getEpisodeByMovieIdAndEpisodeNumber(
//            @PathVariable Long movieId,
//            @PathVariable Integer episodeNumber) {
//        return ResponseEntity.ok(episodeService.findByMovieIdAndEpisodeNumber(movieId, episodeNumber));
//    }
//
//    /**
//     * Lấy tập phim mới nhất của một phim
//     */
//    @GetMapping("/movie/{movieId}/latest")
//    public ResponseEntity<EpisodeDto> getLatestEpisodeByMovieId(@PathVariable Long movieId) {
//        return ResponseEntity.ok(episodeService.findLatestEpisodeByMovieId(movieId));
//    }
//
//    /**
//     * Ghi nhận lượt xem cho tập phim
//     */
//    @PostMapping("/{id}/view")
//    public ResponseEntity<Void> viewEpisode(@PathVariable Long id) {
//        episodeService.incrementViewCount(id);
//        return ResponseEntity.ok().build();
//    }
//}
