package com.datn.motchill.admin.domain.service;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminBaseEntity;
import com.datn.motchill.admin.infrastructure.persistence.entity.AdminQLHoatDongEntity;
import com.datn.motchill.admin.presentation.dto.quanLyHoatDong.AdminQuanLyHoatDongFilterDTO;
import com.datn.motchill.admin.presentation.enums.AdminActionEnum;
import com.datn.motchill.admin.presentation.enums.AdminParamEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Author: vietcd_llq
 * Since:
 * Description: Lịch sử hoạt động
 */
public interface AdminQuanLyHoatDongService {
    Page<AdminQLHoatDongEntity> search(AdminQuanLyHoatDongFilterDTO filterDTO);
    <T> void save(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, Long idBanGhi, T noiDung);
    <T> void save(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, Long idBanGhi, T noiDung, String lyDoTuChoi);
    <T> void saveAll(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids);
    <T> void saveAll(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids, String lyDoTuChoi);
    <T> void update(HttpServletRequest request, AdminParamEnum paramEnum, Long idBanGhiMuonLuu, Long idBanGhiMuonLay);
    <T extends AdminBaseEntity> void saveAllLog(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids);
    <T extends AdminBaseEntity> void saveAllLog(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids, String lyDoTuChoi);
}
