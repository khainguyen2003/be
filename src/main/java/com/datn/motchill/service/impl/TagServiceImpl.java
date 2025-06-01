package com.datn.motchill.service.impl;

import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.tag.TagDto;
import com.datn.motchill.dto.tag.TagRequest;
import com.datn.motchill.entity.Tag;

import com.datn.motchill.repository.TagRepository;
import com.datn.motchill.service.TagService;
import com.datn.motchill.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TagDto> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDTO> findAllOptions() {
        return tagRepository.findAll().stream()
                .map(tag -> new OptionDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto findById(Long id) {
        return tagRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto findBySlug(String slug) {
        return tagRepository.findBySlug(slug)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Tag not found with slug: " + slug));
    }

    @Override
    public TagDto create(TagRequest request) {
        // Check if a tag with the same name already exists
        if (existsByName(request.getName())) {
            throw new IllegalArgumentException("Tag with name '" + request.getName() + "' already exists");
        }
        
        Tag tag = new Tag();
        updateTagFromRequest(tag, request);
        
        // Generate slug if not provided
        if (tag.getSlug() == null || tag.getSlug().isBlank()) {
            tag.setSlug(SlugUtils.slugify(tag.getName()));
        }
        
        // Check if a tag with the same slug already exists
        if (existsBySlug(tag.getSlug())) {
            throw new IllegalArgumentException("Tag with slug '" + tag.getSlug() + "' already exists");
        }
        
        tag = tagRepository.save(tag);
        
        return convertToDto(tag);
    }

    @Override
    public TagDto update(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));
        
        // Check if another tag with the same name already exists
        if (!tag.getName().equals(request.getName()) && existsByName(request.getName())) {
            throw new IllegalArgumentException("Another tag with name '" + request.getName() + "' already exists");
        }
        
        String oldSlug = tag.getSlug();
        updateTagFromRequest(tag, request);
        
        // Generate slug if not provided or if name has changed and slug wasn't explicitly set
        if (tag.getSlug() == null || tag.getSlug().isBlank() || 
            (!tag.getName().equals(request.getName()) && tag.getSlug().equals(oldSlug))) {
            tag.setSlug(SlugUtils.slugify(tag.getName()));
        }
        
        // Check if another tag with the same slug already exists
        if (!oldSlug.equals(tag.getSlug()) && existsBySlug(tag.getSlug())) {
            throw new IllegalArgumentException("Another tag with slug '" + tag.getSlug() + "' already exists");
        }
        
        tag = tagRepository.save(tag);
        
        return convertToDto(tag);
    }

    @Override
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new NotFoundException("Tag not found with id: " + id);
        }
        
        tagRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        return tagRepository.existsBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }
    
    private void updateTagFromRequest(Tag tag, TagRequest request) {
        tag.setName(request.getName());
        
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            tag.setSlug(request.getSlug());
        }
    }
    
    private TagDto convertToDto(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getName(),
                tag.getSlug()
        );
    }
}
