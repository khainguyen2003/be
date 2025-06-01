package com.datn.motchill.controller;

import com.datn.motchill.dto.DirectorDto;
import com.datn.motchill.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for public access to directors
 */
@RestController
@RequestMapping("/api/directors")
@RequiredArgsConstructor
public class PublicDirectorController {
    
    private final DirectorService directorService;
    
    /**
     * Get all directors with pagination
     */
    @GetMapping
    public ResponseEntity<Page<DirectorDto>> getAllDirectors(Pageable pageable) {
        return ResponseEntity.ok(directorService.findAll(pageable));
    }
    
    /**
     * Get all directors without pagination
     */
    @GetMapping("/all")
    public ResponseEntity<List<DirectorDto>> getAllDirectors() {
        return ResponseEntity.ok(directorService.findAll());
    }
    
    /**
     * Get a director by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<DirectorDto> getDirectorById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.findById(id));
    }
    
    /**
     * Get a director by slug
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<DirectorDto> getDirectorBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(directorService.findBySlug(slug));
    }
}
