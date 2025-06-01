package com.datn.motchill.service;

import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.genre.GenreDto;
import com.datn.motchill.dto.genre.GenreRequest;
import com.datn.motchill.dto.OptionDTO;

import com.datn.motchill.dto.genre.GenreFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenreService {
    
    /**
     * Tìm tất cả thể loại với phân trang
     */
    Page<GenreDto> findAll(Pageable pageable);

    Page<GenreDto> search(GenreFilter filter);
    
    /**
     * Lấy danh sách tất cả thể loại
     */
    List<GenreDto> findAll();

    List<OptionDTO> findAllOptions();
    
    /**
     * Tìm thể loại theo ID
     */
    GenreDto findById(Long id);
    
    /**
     * Tìm thể loại theo slug
     */
    GenreDto findBySlug(String slug);
    
    /**
     * Tạo thể loại mới
     */
    GenreDto create(GenreRequest request);
    
    /**
     * Cập nhật thể loại
     */
    GenreDto update(Long id, GenreRequest request);
    
    /**
     * Xóa thể loại
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
