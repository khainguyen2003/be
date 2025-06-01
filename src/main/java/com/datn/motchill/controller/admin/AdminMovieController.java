package com.datn.motchill.controller.admin;

import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.movie.MovieDto;
import com.datn.motchill.dto.movie.MovieFilterDTO;
import com.datn.motchill.dto.movie.MovieRequest;
import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller for admin to manage movies
 */
@RestController
@RequestMapping("/api/admin/movies")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {
    
    private final MovieService movieService;

    private final ObjectMapper objectMapper;

    @PostMapping("/search")
    public ResponseEntity<Page<MovieDto>> search(@RequestBody MovieFilterDTO filter) {
        return ResponseEntity.ok(movieService.searchAdmin(filter));
    }
    
    /**
     * Create a new movie
     */
    @PostMapping(value = "/save-draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MovieDto> createMovie(
        @RequestParam("movieData") String movieDataJson,
        @RequestPart(value = "thumbImage", required = false) MultipartFile thumbImage,
        @RequestPart(value = "posterImage", required = false) MultipartFile posterImage
    ) throws IOException {
        MovieRequest movieData = objectMapper.readValue(movieDataJson, MovieRequest.class);
        movieData.setThumb(thumbImage);
        movieData.setPoster(posterImage);
        return new ResponseEntity<>(movieService.saveDraft(movieData), HttpStatus.CREATED);
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
    @PutMapping("/update-draft/{id}")
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
