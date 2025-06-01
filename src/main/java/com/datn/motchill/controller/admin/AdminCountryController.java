package com.datn.motchill.controller.admin;

import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.country.CountryDto;
import com.datn.motchill.dto.country.CountryRequest;
import com.datn.motchill.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing countries (admin endpoints)
 */
@RestController
@RequestMapping("/api/admin/countries")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminCountryController {

    private final CountryService countryService;

    /**
     * Get all countries with pagination
     */
    @GetMapping
    public ResponseEntity<Page<CountryDto>> getAllCountries(Pageable pageable) {
        return ResponseEntity.ok(countryService.findAll(pageable));
    }

    /**
     * Get all country options
     */
    @GetMapping("/options")
    public ResponseEntity<List<OptionDTO>> getAllCountryOptions() {
        return ResponseEntity.ok(countryService.findAllOptions());
    }

    /**
     * Get a country by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.findById(id));
    }

    /**
     * Create a new country
     */
    @PostMapping
    public ResponseEntity<CountryDto> createCountry(@Valid @RequestBody CountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(countryService.create(request));
    }

    /**
     * Update an existing country
     */
    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(
            @PathVariable Long id,
            @Valid @RequestBody CountryRequest request) {
        return ResponseEntity.ok(countryService.update(id, request));
    }

    /**
     * Delete a country
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if a country with given name exists
     */
    @GetMapping("/check/name")
    public ResponseEntity<Boolean> checkCountryNameExists(@RequestParam String name) {
        return ResponseEntity.ok(countryService.existsByName(name));
    }

    /**
     * Check if a country with given slug exists
     */
    @GetMapping("/check/slug")
    public ResponseEntity<Boolean> checkCountrySlugExists(@RequestParam String slug) {
        return ResponseEntity.ok(countryService.existsBySlug(slug));
    }

    /**
     * Check if a country with given name or slug exists
     */
    @GetMapping("/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String slug) {
        boolean nameExists = name != null && !name.isBlank() && countryService.existsByName(name);
        boolean slugExists = slug != null && !slug.isBlank() && countryService.existsBySlug(slug);

        return ResponseEntity.ok(Map.of(
                "nameExists", nameExists,
                "slugExists", slugExists
        ));
    }
}
