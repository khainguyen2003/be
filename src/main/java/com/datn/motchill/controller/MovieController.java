package com.datn.motchill.controller;

import com.datn.motchill.dto.movie.MovieRequestDto;
import com.datn.motchill.dto.movie.MovieResponseDto;
import com.datn.motchill.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // Thêm mới phim
    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@RequestBody @Valid MovieRequestDto dto) {
        return ResponseEntity.ok(movieService.createMovie(dto));
    }

    // Cập nhật phim
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(@PathVariable Long id, @RequestBody @Valid MovieRequestDto dto) {
        return ResponseEntity.ok(movieService.updateMovie(id, dto));
    }

    // Xóa phim
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().body("Xóa phim thành công");
    }

    // (Tuỳ chọn) Lấy chi tiết phim
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
}