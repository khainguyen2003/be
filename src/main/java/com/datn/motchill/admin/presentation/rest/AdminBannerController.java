package com.datn.motchill.admin.presentation.rest;

import com.datn.motchill.admin.presentation.dto.banner.AdminBannerResponseDto;
import com.datn.motchill.admin.domain.service.AdminBannerService;
import com.datn.motchill.admin.common.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.API_PATH.API_ADMIN_PATH.ADMIN_BANNER)
public class AdminBannerController {
    private final AdminBannerService adminBannerService;

    @GetMapping("/search")
    public List<AdminBannerResponseDto> search() {
        return this.adminBannerService.search(null);
    }

}
