package com.datn.motchill.controller;

import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.tag.TagDto;
import com.datn.motchill.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for public access to tags
 */
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class PublicTagController {
    
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
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }
    
    /**
     * Get a tag by slug
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<TagDto> getTagBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(tagService.findBySlug(slug));
    }
}
