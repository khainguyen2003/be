package com.datn.motchill.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.country.CountryDto;
import com.datn.motchill.dto.country.CountryRequest;
import com.datn.motchill.entity.Country;
import com.datn.motchill.repository.CountryRepository;
import com.datn.motchill.service.CountryService;
import com.datn.motchill.util.SlugUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CountryDto> findAll(Pageable pageable) {
        return countryRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDTO> findAllOptions() {
        return countryRepository.findAll().stream()
                .map(country -> new OptionDTO(country.getId(), country.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> findAll() {
        return countryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto findById(Long id) {
        return countryRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Country not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto findBySlug(String slug) {
        return countryRepository.findBySlug(slug)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Country not found with slug: " + slug));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CountryDto create(CountryRequest request) {
        Country country = new Country();
        updateCountryFromRequest(country, request);
        
        // Generate slug if not provided
        if (country.getSlug() == null || country.getSlug().isBlank()) {
            country.setSlug(SlugUtils.slugify(country.getName()));
        }
        
        country = countryRepository.save(country);
        
        return convertToDto(country);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CountryDto update(Long id, CountryRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Country not found with id: " + id));
        
        updateCountryFromRequest(country, request);
        
        // Generate slug if not provided
        if (country.getSlug() == null || country.getSlug().isBlank()) {
            country.setSlug(SlugUtils.slugify(country.getName()));
        }
        
        country = countryRepository.save(country);
        
        return convertToDto(country);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new NotFoundException("Country not found with id: " + id);
        }
        
        countryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        return countryRepository.existsBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return countryRepository.existsByName(name);
    }
    
    private void updateCountryFromRequest(Country country, CountryRequest request) {
        country.setName(request.getName());
        
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            country.setSlug(request.getSlug());
        }
    }
    
    private CountryDto convertToDto(Country country) {
        return new CountryDto(
                country.getId(),
                country.getName(),
                country.getSlug()
        );
    }
}
