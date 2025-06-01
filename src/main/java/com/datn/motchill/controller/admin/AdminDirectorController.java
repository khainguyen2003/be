package com.datn.motchill.controller.admin;

import com.datn.motchill.dto.AdminBaseResponse;
import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.DirectorDto;
import com.datn.motchill.dto.DirectorRequest;
import com.datn.motchill.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing directors (admin endpoints)
 */
@RestController
@RequestMapping("/api/admin/directors")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminDirectorController {
    
    private final DirectorService directorService;
    
    /**
     * Get all directors with pagination
     */
    @GetMapping
    public ResponseEntity<Page<DirectorDto>> getAllDirectors(Pageable pageable) {
        return ResponseEntity.ok(directorService.findAll(pageable));
    }
    
    /**
     * Get a director by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<DirectorDto> getDirectorById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.findById(id));
    }

    /**
     * Create a new director
     */
    @PostMapping("/save-draft")
    public ResponseEntity<DirectorDto> createDirector(@Valid @RequestBody DirectorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(directorService.create(request));
    }
    
    /**
     * Update an existing director
     */
    @PutMapping("/update-draft/{id}")
    public ResponseEntity<DirectorDto> updateDirector(
            @PathVariable Long id,
            @Valid @RequestBody DirectorRequest request) {
        return ResponseEntity.ok(directorService.update(id, request));
    }
    
    /**
     * Delete a director
     */
    @DeleteMapping("/delete")
    public ResponseEntity<AdminBaseResponse> deleteDirector(@RequestBody AdminIdsDTO idsDTO) {
        String res = directorService.delete(idsDTO);
        AdminBaseResponse response = new AdminBaseResponse().success(res);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check if a director with given name exists
     */
    @GetMapping("/check/name")
    public ResponseEntity<Boolean> checkDirectorNameExists(@RequestParam String name) {
        return ResponseEntity.ok(directorService.existsByName(name));
    }
    
    /**
     * Check if a director with given slug exists
     */
    @GetMapping("/check/slug")
    public ResponseEntity<Boolean> checkDirectorSlugExists(@RequestParam String slug) {
        return ResponseEntity.ok(directorService.existsBySlug(slug));
    }
}
