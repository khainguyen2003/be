package com.datn.motchill.controller.admin;

import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.tag.TagDto;
import com.datn.motchill.dto.tag.TagRequest;
import com.datn.motchill.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing tags (admin endpoints)
 */
@RestController
@RequestMapping("/api/admin/tags")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminTagController {
    
    private final TagService tagService;
    
    /**
     * Get all tags with pagination
     */
    @GetMapping
    public ResponseEntity<Page<TagDto>> getAllTags(Pageable pageable) {
        return ResponseEntity.ok(tagService.findAll(pageable));
    }

    @GetMapping("/options")
    public ResponseEntity<List<OptionDTO>> getAllTagsOptions() {
        return ResponseEntity.ok(tagService.findAllOptions());
    }
    
    /**
     * Get a tag by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<com.datn.motchill.dto.tag.TagDto> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }
    
    /**
     * Create a new tag
     */
    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.create(request));
    }
    
    /**
     * Update an existing tag
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.update(id, request));
    }
    
    /**
     * Delete a tag
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Check if a tag with given name exists
     */
    @GetMapping("/check/name")
    public ResponseEntity<Boolean> checkTagNameExists(@RequestParam String name) {
        return ResponseEntity.ok(tagService.existsByName(name));
    }
    
    /**
     * Check if a tag with given slug exists
     */
    @GetMapping("/check/slug")
    public ResponseEntity<Boolean> checkTagSlugExists(@RequestParam String slug) {
        return ResponseEntity.ok(tagService.existsBySlug(slug));
    }
}
