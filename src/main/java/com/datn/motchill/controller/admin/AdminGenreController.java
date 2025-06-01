package com.datn.motchill.controller.admin;

import com.datn.motchill.dto.AdminBaseResponse;
import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.genre.GenreDto;
import com.datn.motchill.dto.genre.GenreRequest;
import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.genre.GenreFilter;
import com.datn.motchill.entity.BaseEntity;
import com.datn.motchill.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing genres (admin endpoints)
 */
@RestController
@RequestMapping("/api/admin/genres")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminGenreController {
    
    private final GenreService genreService;
    
    /**
     * Get all genres with pagination
     */
    @GetMapping
    public ResponseEntity<Page<GenreDto>> getAllGenres(Pageable pageable) {
        return ResponseEntity.ok(genreService.findAll(pageable));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<GenreDto>> search(@Valid @RequestBody GenreFilter filter) {
        return ResponseEntity.ok(genreService.search(filter));
    }
    
    /**
     * Get a genre by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.findById(id));
    }

    @PostMapping("/options")
    public ResponseEntity<List<OptionDTO>> getAllOption() {
        return ResponseEntity.ok(genreService.findAllOptions());
    }
    
    /**
     * Create a new genre
     */
    @PostMapping("/save-draft")
    public ResponseEntity<GenreDto> createGenre(@Valid @RequestBody GenreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreService.create(request));
    }
    
    /**
     * Update an existing genre
     */
    @PutMapping("/update-draft/{id}")
    public ResponseEntity<GenreDto> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody GenreRequest request) {
        return ResponseEntity.ok(genreService.update(id, request));
    }
    
    /**
     * Delete a genre
     */
    @PostMapping("/delete")
    public ResponseEntity<AdminBaseResponse> deleteGenre(@RequestBody AdminIdsDTO idsDTO) {
        String res = genreService.delete(idsDTO);
        AdminBaseResponse response = new AdminBaseResponse().success(res);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check if a genre with given name exists
     */
    @GetMapping("/check/name")
    public ResponseEntity<Boolean> checkGenreNameExists(@RequestParam String name) {
        return ResponseEntity.ok(genreService.existsByName(name));
    }
    
    /**
     * Check if a genre with given slug exists
     */
    @GetMapping("/check/slug")
    public ResponseEntity<Boolean> checkGenreSlugExists(@RequestParam String slug) {
        return ResponseEntity.ok(genreService.existsBySlug(slug));
    }
}
