package com.datn.motchill.controller.admin;

import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.movie.MovieDto;
import com.datn.motchill.dto.movie.MovieFilterDTO;
import com.datn.motchill.dto.movie.MovieRequest;
import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for admin to manage movies
 */
@RestController
@RequestMapping("/api/admin/movies")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {
    
    private final MovieService movieService;

    @PostMapping("/search")
    public ResponseEntity<Page<MovieDto>> search(@RequestBody MovieFilterDTO filter) {
        return ResponseEntity.ok(movieService.searchAdmin(filter));
    }
    
    /**
     * Create a new movie
     */
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        return new ResponseEntity<>(movieService.saveDraft(movieRequest), HttpStatus.CREATED);
    }
    
    /**
     * Get a movie by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findById(id));
    }
    
    /**
     * Update a movie
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest movieRequest) {
        return ResponseEntity.ok(movieService.updateDraft(id, movieRequest));
    }
    
    /**
     * Delete a movie
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@RequestBody @Valid AdminIdsDTO ids, @PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Cập nhật trạng thái phim
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<MovieDto> updateStatus(@PathVariable Long id, @RequestParam MovieStatusEnum status) {
        return ResponseEntity.ok(movieService.updateStatus(id, status));
    }
}
