package com.datn.motchill.controller;

import com.datn.motchill.dto.genre.GenreDto;
import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for public genre API endpoints
 */
@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class PublicGenreController {
    
    private final GenreService genreService;
    
    /**
     * Lấy tất cả thể loại với phân trang
     */
    @GetMapping
    public ResponseEntity<Page<GenreDto>> getAllGenres(Pageable pageable) {
        return ResponseEntity.ok(genreService.findAll(pageable));
    }
    
    /**
     * Lấy tất cả thể loại
     */
    @GetMapping("/all")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAll());
    }

    /**
     * Lấy danh sách options cho thể loại
     */
    @GetMapping("/options")
    public ResponseEntity<List<OptionDTO>> getAllOptions() {
        return ResponseEntity.ok(genreService.findAllOptions());
    }
    
    /**
     * Lấy thể loại theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.findById(id));
    }
    
    /**
     * Lấy thể loại theo slug
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<GenreDto> getGenreBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(genreService.findBySlug(slug));
    }
}
