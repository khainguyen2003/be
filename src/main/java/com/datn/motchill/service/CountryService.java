package com.datn.motchill.service;

import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.country.CountryDto;
import com.datn.motchill.dto.country.CountryRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryService {
    
    /**
     * Tìm tất cả quốc gia với phân trang
     */
    Page<CountryDto> findAll(Pageable pageable);
    
    /**
     * Lấy danh sách tất cả quốc gia
     */
    List<CountryDto> findAll();

    List<OptionDTO> findAllOptions();
    
    /**
     * Tìm quốc gia theo ID
     */
    CountryDto findById(Long id);
    
    /**
     * Tìm quốc gia theo slug
     */
    CountryDto findBySlug(String slug);
    
    /**
     * Tạo quốc gia mới
     */
    CountryDto create(CountryRequest request);
    
    /**
     * Cập nhật quốc gia
     */
    CountryDto update(Long id, CountryRequest request);
    
    /**
     * Xóa quốc gia
     */
    void delete(Long id);
    
    /**
     * Kiểm tra tồn tại theo slug
     */
    boolean existsBySlug(String slug);
    
    /**
     * Kiểm tra tồn tại theo tên
     */
    boolean existsByName(String name);
}
