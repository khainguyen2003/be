package com.datn.motchill.admin.domain.service.impl;

import com.datn.motchill.admin.common.utils.Utils;
import com.datn.motchill.admin.domain.service.AdminQuanLyHoatDongService;
import com.datn.motchill.admin.infrastructure.persistence.entity.AdminBaseEntity;
import com.datn.motchill.admin.infrastructure.persistence.entity.AdminQLHoatDongEntity;
import com.datn.motchill.admin.infrastructure.persistence.repository.QuanLyHoatDongRepository;
import com.datn.motchill.admin.presentation.dto.quanLyHoatDong.AdminQuanLyHoatDongFilterDTO;
import com.datn.motchill.admin.presentation.enums.AdminActionEnum;
import com.datn.motchill.admin.presentation.enums.AdminParamEnum;
import com.datn.motchill.admin.common.exceptions.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
     * Author: vietcd_llq
     * Since:
     * Description: Lịch sử hoạt động
*/
@Service
@RequiredArgsConstructor
public class AdminQuanLyHoatDongServiceImpl implements AdminQuanLyHoatDongService {

    private final QuanLyHoatDongRepository quanLyHoatDongRepository;

    @Override
    public Page<AdminQLHoatDongEntity> search(AdminQuanLyHoatDongFilterDTO filterDTO) {
        if (null == filterDTO.getIdBanGhi()) {
            throw new BadRequestException("Id bản ghi không được để trống");
        }
        Sort sort = Utils.generatedSort(filterDTO.getSort());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getLimit(), sort);
        Page<AdminQLHoatDongEntity> page = quanLyHoatDongRepository.findAllByIdBanGhiAndChucNang(filterDTO.getIdBanGhi(), AdminParamEnum.fromKey(filterDTO.getChucNang()) , pageable);

        return page;
    }

    @Override
    public <T> void save(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, Long idBanGhi, T noiDung) {
        AdminQLHoatDongEntity adminQlHoatDongEntity = qlHoatDongEntity(adminActionEnum, paramEnum, request, idBanGhi);
        if (null != noiDung) {
            adminQlHoatDongEntity.setNoiDung(Utils.convertToJson(noiDung));
        }
        quanLyHoatDongRepository.save(adminQlHoatDongEntity);
    }

    @Override
    @Transactional
    public  <T> void saveAll(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids){
        List<AdminQLHoatDongEntity> listBanGhi = new ArrayList<>();

        banGhi.forEach(item -> {
            Long id = ids.apply(item);
            AdminQLHoatDongEntity adminQlHoatDongEntity = qlHoatDongEntity(adminActionEnum, paramEnum, request, id);
            adminQlHoatDongEntity.setNoiDung(Utils.convertToJson(item));
            listBanGhi.add(adminQlHoatDongEntity);
        });
        if (!listBanGhi.isEmpty()) {
            quanLyHoatDongRepository.saveAll(listBanGhi);
        }

    }

    @Override
    public <T> void saveAll(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids, String lyDoTuChoi) {
        List<AdminQLHoatDongEntity> listBanGhi = new ArrayList<>();
        banGhi.forEach(item -> {
            Long id = ids.apply(item);
            AdminQLHoatDongEntity adminQlHoatDongEntity = qlHoatDongEntity(adminActionEnum, paramEnum, request, id);
            adminQlHoatDongEntity.setNoiDung(Utils.convertToJson(item));
            adminQlHoatDongEntity.setLyDoTuChoi(lyDoTuChoi);
            listBanGhi.add(adminQlHoatDongEntity);
        });
        if (!listBanGhi.isEmpty()) {
            quanLyHoatDongRepository.saveAll(listBanGhi);
        }
    }

    @Override
    public <T> void update(HttpServletRequest request, AdminParamEnum paramEnum, Long idBanGhiMuonLuu, Long idBanGhiMuonLay) {
        List<AdminQLHoatDongEntity> listBanGhiCu = quanLyHoatDongRepository.findAllByIdBanGhiAndChucNang(idBanGhiMuonLay, paramEnum);
        quanLyHoatDongRepository.updateListByIdBanGhi(idBanGhiMuonLuu, listBanGhiCu);
    }

    @Override
  //  @Transactional
    public <T extends AdminBaseEntity> void saveAllLog(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids) {
        List<AdminQLHoatDongEntity> listBanGhi = new ArrayList<>();

        banGhi.forEach(item -> {
          //  entityManager.detach(item);
            Long id = ids.apply(item);
            AdminQLHoatDongEntity adminQlHoatDongEntity = qlHoatDongEntity(adminActionEnum, paramEnum, request, id);
            adminQlHoatDongEntity.setNoiDung(Utils.convertToJson(item));
            listBanGhi.add(adminQlHoatDongEntity);
        });
        if (!listBanGhi.isEmpty()) {
            quanLyHoatDongRepository.saveAll(listBanGhi);
        }
    }

    @Override
    public <T extends AdminBaseEntity> void saveAllLog(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, List<T> banGhi, Function<T, Long> ids, String lyDoTuChoi) {
        List<AdminQLHoatDongEntity> listBanGhi = new ArrayList<>();
        banGhi.forEach(item -> {
           // entityManager.detach(item);
            Long id = ids.apply(item);
            AdminQLHoatDongEntity adminQlHoatDongEntity = qlHoatDongEntity(adminActionEnum, paramEnum, request, id);
            adminQlHoatDongEntity.setNoiDung(Utils.convertToJson(item));
            adminQlHoatDongEntity.setLyDoTuChoi(lyDoTuChoi);
            listBanGhi.add(adminQlHoatDongEntity);
        });
        if (!listBanGhi.isEmpty()) {
            quanLyHoatDongRepository.saveAll(listBanGhi);
        }
    }

    @Override
    public <T> void save(HttpServletRequest request, AdminParamEnum paramEnum, AdminActionEnum adminActionEnum, Long idBanGhi, T noiDung, String lyDoTuChoi) {
        AdminQLHoatDongEntity adminQlHoatDongEntity = qlHoatDongEntity(adminActionEnum, paramEnum, request, idBanGhi);
        if (null != noiDung) {
            adminQlHoatDongEntity.setNoiDung(Utils.convertToJson(noiDung));
        }
        adminQlHoatDongEntity.setLyDoTuChoi(lyDoTuChoi);
        quanLyHoatDongRepository.save(adminQlHoatDongEntity);
    }

    private AdminQLHoatDongEntity qlHoatDongEntity(AdminActionEnum action, AdminParamEnum paramEnum, HttpServletRequest request, Long idBanGhi) {
        AdminQLHoatDongEntity adminQlHoatDongEntity = new AdminQLHoatDongEntity();
        adminQlHoatDongEntity.setChucNang(paramEnum);
        adminQlHoatDongEntity.setIP(Utils.extractClientIdAddr(request));
        adminQlHoatDongEntity.setNgayThucHien(new Timestamp(new Date().getTime()));
//        qlHoatDongEntity.setNguoiDung(Utils.getUserId());
        adminQlHoatDongEntity.setThaoTac(action);
        if (idBanGhi != null) {
            adminQlHoatDongEntity.setIdBanGhi(idBanGhi);
        }
        return adminQlHoatDongEntity;
    }
}
