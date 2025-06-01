package com.datn.motchill.service;

import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.DirectorDto;
import com.datn.motchill.dto.DirectorRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DirectorService {
    
    /**
     * Tìm tất cả đạo diễn với phân trang
     */
    Page<DirectorDto> findAll(Pageable pageable);
    
    /**
     * Lấy danh sách tất cả đạo diễn
     */
    List<DirectorDto> findAll();
    
    /**
     * Tìm đạo diễn theo ID
     */
    DirectorDto findById(Long id);
    
    /**
     * Tìm đạo diễn theo slug
     */
    DirectorDto findBySlug(String slug);
    
    /**
     * Tạo đạo diễn mới
     */
    DirectorDto create(DirectorRequest request);
    
    /**
     * Cập nhật đạo diễn
     */
    DirectorDto update(Long id, DirectorRequest request);
    
    /**
     * Xóa đạo diễn
     */
    String delete(AdminIdsDTO idsDTO);
    
    /**
     * Kiểm tra tồn tại theo slug
     */
    boolean existsBySlug(String slug);
    
    /**
     * Kiểm tra tồn tại theo tên
     */
    boolean existsByName(String name);
}
