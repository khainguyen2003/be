package com.datn.motchill.admin.domain.service;

import com.datn.motchill.admin.presentation.dto.banner.AdminBannerFilterDto;
import com.datn.motchill.admin.presentation.dto.banner.AdminBannerResponseDto;

import java.util.List;

public interface AdminBannerService {
    List<AdminBannerResponseDto> search(AdminBannerFilterDto filter);
}
