package com.datn.motchill.controller;

import com.datn.motchill.dto.genre.GenreDto;
import com.datn.motchill.dto.genre.GenreRequest;
import com.datn.motchill.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenre(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    @PostMapping
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreRequest request) {
        return ResponseEntity.ok(genreService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable Long id, @RequestBody GenreRequest request) {
        return ResponseEntity.ok(genreService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
