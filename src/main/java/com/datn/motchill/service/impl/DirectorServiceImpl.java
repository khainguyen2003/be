package com.datn.motchill.service.impl;

import com.datn.motchill.common.exceptions.BadRequestException;
import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.common.utils.Constants;
import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.DirectorDto;
import com.datn.motchill.dto.DirectorRequest;
import com.datn.motchill.entity.Director;
import com.datn.motchill.entity.Director;
import com.datn.motchill.repository.DirectorRepository;
import com.datn.motchill.service.DirectorService;
import com.datn.motchill.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DirectorDto> findAll(Pageable pageable) {
        return directorRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectorDto> findAll() {
        return directorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DirectorDto findById(Long id) {
        return directorRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Director not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public DirectorDto findBySlug(String slug) {
        return directorRepository.findBySlug(slug)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Director not found with slug: " + slug));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirectorDto create(DirectorRequest request) {
        // Check if a director with the same name already exists
        if (existsByName(request.getName())) {
            throw new IllegalArgumentException("Director with name '" + request.getName() + "' already exists");
        }
        
        Director director = new Director();
        updateDirectorFromRequest(director, request);
        
        // Generate slug if not provided
        if (director.getSlug() == null || director.getSlug().isBlank()) {
            director.setSlug(SlugUtils.slugify(director.getName()));
        }
        
        // Check if a director with the same slug already exists
        if (existsBySlug(director.getSlug())) {
            throw new IllegalArgumentException("Director with slug '" + director.getSlug() + "' already exists");
        }
        
        director = directorRepository.save(director);
        
        return convertToDto(director);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirectorDto update(Long id, DirectorRequest request) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Director not found with id: " + id));
        
        // Check if another director with the same name already exists
        if (!director.getName().equals(request.getName()) && existsByName(request.getName())) {
            throw new IllegalArgumentException("Another director with name '" + request.getName() + "' already exists");
        }
        
        String oldSlug = director.getSlug();
        updateDirectorFromRequest(director, request);
        
        // Generate slug if not provided or if name has changed and slug wasn't explicitly set
        if (director.getSlug() == null || director.getSlug().isBlank() ||
            (!director.getName().equals(request.getName()) && director.getSlug().equals(oldSlug))) {
            director.setSlug(SlugUtils.slugify(director.getName()));
        }
        
        // Check if another director with the same slug already exists
        if (!oldSlug.equals(director.getSlug()) && existsBySlug(director.getSlug())) {
            throw new IllegalArgumentException("Another director with slug '" + director.getSlug() + "' already exists");
        }
        
        director = directorRepository.save(director);
        
        return convertToDto(director);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(AdminIdsDTO idsDTO) {
        List<Long> listIds = Arrays.stream(idsDTO.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toUnmodifiableList());
        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<Director> directorList = directorRepository.findAllById(listIds);
        if (ObjectUtils.isEmpty(directorList)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        directorRepository.deleteAllByIdInBatch(listIds);
        return String.format("Xóa thành công %d/%d đạo diễn phim!", directorList.size(), directorList.size());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        return directorRepository.existsBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return directorRepository.existsByName(name);
    }
    
    private void updateDirectorFromRequest(Director director, DirectorRequest request) {
        director.setName(request.getName());
        
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            director.setSlug(request.getSlug());
        }
    }
    
    private DirectorDto convertToDto(Director director) {
        return new DirectorDto(
                director.getId(),
                director.getName(),
                director.getSlug()
        );
    }
}
