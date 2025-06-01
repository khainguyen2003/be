package com.datn.motchill.service;

import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.tag.TagDto;
import com.datn.motchill.dto.tag.TagRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    
    /**
     * Tìm tất cả tag với phân trang
     */
    Page<TagDto> findAll(Pageable pageable);
    
    List<OptionDTO> findAllOptions();
    
    /**
     * Tìm tag theo ID
     */
    TagDto findById(Long id);
    
    /**
     * Tìm tag theo slug
     */
    TagDto findBySlug(String slug);
    
    TagDto create(TagRequest request);
    
    /**
     * Cập nhật tag
     */
    TagDto update(Long id, TagRequest request);
    
    /**
     * Xóa tag
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
