package com.datn.motchill.admin.domain.service.impl;

import com.datn.motchill.admin.presentation.dto.banner.AdminBannerFilterDto;
import com.datn.motchill.admin.presentation.dto.banner.AdminBannerResponseDto;
import com.datn.motchill.admin.infrastructure.persistence.repository.AdminBannerRepository;
import com.datn.motchill.admin.domain.service.AdminBannerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBannerServiceImpl implements AdminBannerService {
    private final AdminBannerRepository adminBannerRepository;
    private final ObjectMapper mapper;

    @Override
    public List<AdminBannerResponseDto> search(AdminBannerFilterDto filter) {

        return adminBannerRepository.findAll().stream().map(
                banner ->
                        mapper.convertValue(banner, AdminBannerResponseDto.class)).collect(Collectors.toList());
    }
}
