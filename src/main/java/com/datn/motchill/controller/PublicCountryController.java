package com.datn.motchill.controller;

import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.country.CountryDto;
import com.datn.motchill.service.CountryService;
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
 * Controller for public country API endpoints
 */
@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class PublicCountryController {
    
    private final CountryService countryService;
    
    /**
     * Lấy tất cả quốc gia với phân trang
     */
    @GetMapping
    public ResponseEntity<Page<CountryDto>> getAllCountries(Pageable pageable) {
        return ResponseEntity.ok(countryService.findAll(pageable));
    }
    
    /**
     * Lấy tất cả quốc gia
     */
    @GetMapping("/all")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity.ok(countryService.findAll());
    }
    
    /**
     * Lấy tất cả quốc gia dạng options (id, name) cho dropdown
     */
    @GetMapping("/options")
    public ResponseEntity<List<OptionDTO>> getAllCountryOptions() {
        return ResponseEntity.ok(countryService.findAllOptions());
    }
    
    /**
     * Lấy quốc gia theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.findById(id));
    }
    
    /**
     * Lấy quốc gia theo slug
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<CountryDto> getCountryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(countryService.findBySlug(slug));
    }
}
