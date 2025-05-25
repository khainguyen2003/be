package com.datn.motchill.admin.domain.service.impl;

import com.datn.motchill.admin.common.utils.*;
import com.datn.motchill.admin.domain.service.AdminGenreService;
import com.datn.motchill.admin.domain.service.AdminQuanLyHoatDongService;
import com.datn.motchill.admin.infrastructure.persistence.entity.AdminGenreEntity;
import com.datn.motchill.admin.infrastructure.persistence.repository.AdminGenreRepository;
import com.datn.motchill.admin.presentation.dto.AdminIdsDTO;
import com.datn.motchill.admin.presentation.dto.AdminOptionDTO;
import com.datn.motchill.admin.presentation.dto.AdminRejectDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreCreateDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreFilterDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreResponseDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreUpdateDTO;
import com.datn.motchill.admin.presentation.enums.AdminParamEnum;
import com.datn.motchill.admin.presentation.enums.AdminStatusEnum;
import com.datn.motchill.admin.common.utils.Constants;
import com.datn.motchill.admin.common.exceptions.BadRequestException;
import com.datn.motchill.admin.common.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminGenreServiceImpl implements AdminGenreService {
    private final AdminGenreRepository adminGenreRepository;
    private final AdminQuanLyHoatDongService adminQuanLyHoatDongService;

    @PersistenceContext
    private EntityManager entityManager;

    private final String GENRE_ALREADY_EXISTS = "Thể loại đã tồn tại!";
    private final String ALL_GENRE_ALREADY_EXISTS = "Tất cả thể loại được chọn đều bị trùng với bản ghi khác!";

    @Override
    public AdminGenreResponseDTO getGenreById(Long id) {
        return ObjectMapperUtils.map(adminGenreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_ID_DA_CHON)),AdminGenreResponseDTO.class);
    }

    @Override
    public Set<AdminOptionDTO> getActiveGenreOptions(AdminGenreFilterDTO filterDTO) {
        return Set.of();
    }

    @Override
    public void validateActiveGenres(String genreCodes, AdminGenreFilterDTO filter) {

    }

    @Override
    public List<AdminGenreEntity> getActiveGenreEntityByListGenreCode(Set<String> genreCodes, AdminGenreFilterDTO filter) {
        return List.of();
    }

    @Override
    public Page<AdminGenreResponseDTO> search(AdminGenreFilterDTO filter) {
        Sort sort = Utils.generatedSort(filter.getSort());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit(), sort);

        Specification<AdminGenreEntity> specification = getSearchSpecification(filter);

        return adminGenreRepository.findAll(specification, pageable).map(entity -> ObjectMapperUtils.map(entity, AdminGenreResponseDTO.class));
    }

    @Override
    public Long saveDraft(AdminGenreCreateDTO createDTO, HttpServletRequest filter) {
        if(adminGenreRepository.isExist(createDTO.getName(), createDTO.getTitle())) {
            throw new BadRequestException(GENRE_ALREADY_EXISTS);
        }

        AdminGenreEntity newEntity = ObjectMapperUtils.map(createDTO, AdminGenreEntity.class);
        newEntity.setStatusSaveDraft();
        AdminGenreEntity entityInDB = adminGenreRepository.save(newEntity);
        // lưu log họat động
        return entityInDB.getId();
    }

    @Override
    public Long saveAndApprove(AdminGenreCreateDTO createDTO, HttpServletRequest filter) {
        if(adminGenreRepository.isExist(createDTO.getName(), createDTO.getTitle())) {
            throw new BadRequestException(GENRE_ALREADY_EXISTS);
        }

        AdminGenreEntity newEntity = ObjectMapperUtils.map(createDTO, AdminGenreEntity.class);
        newEntity.setStatusSaveAndApprove();
        AdminGenreEntity entityInDB = adminGenreRepository.save(newEntity);


        return entityInDB.getId();
    }

    @Override
    public Long saveAndSendApproval(AdminGenreCreateDTO createDTO, HttpServletRequest filter) {
        if(adminGenreRepository.isExist(createDTO.getName(), createDTO.getTitle())) {
            throw new BadRequestException(GENRE_ALREADY_EXISTS);
        }

        AdminGenreEntity newEntity = ObjectMapperUtils.map(createDTO, AdminGenreEntity.class);
        newEntity.setStatusSaveAndSendApprove();
        AdminGenreEntity entityInDB = adminGenreRepository.save(newEntity);

        return entityInDB.getId();
    }

    @Override
    public Long updateDraft(AdminGenreUpdateDTO updateDTO, HttpServletRequest filter) {
        if(updateDTO.getId() == null) {
            throw new BadRequestException(Constants.ID_KHONG_DUOC_DE_TRONG);
        }

        AdminGenreEntity foundEntity = adminGenreRepository.findById(updateDTO.getId()).orElseThrow(
            () -> new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_ID_DA_CHON)
        );

        if(adminGenreRepository.isExistAndIdNot(updateDTO.getName(), updateDTO.getTitle(), updateDTO.getId())) {
            throw new BadRequestException(GENRE_ALREADY_EXISTS);
        }

        ObjectMapperUtils.map(updateDTO, foundEntity);
        if (foundEntity.getStatus().equals(AdminStatusEnum.SAVE_DRAF)) {
            foundEntity.setStatusSaveDraft();
        } else {
            entityManager.detach(foundEntity);
            // Nếu là trạng thái IsActiveEnum.ACTIVE thì chỉ được update trường newData
            // Chỉ update những cột được phép

            foundEntity.setNewData(null);
            String jsonData = Utils.convertToJson(foundEntity);
            foundEntity.setNewData(jsonData);
            // Giữ nguyên trạng thái tham số là CANCEL_APPROVE, Giữ nguyên trạng thái hoạt động hiện tại
            adminGenreRepository.updateNewData(jsonData, updateDTO.getId());
        }
        
        return foundEntity.getId();
    }

    @Override
    public Long updateAndSendApproval(AdminGenreUpdateDTO updateDTO, HttpServletRequest filter) {
        if(updateDTO.getId() == null) {
            throw new BadRequestException(Constants.ID_KHONG_DUOC_DE_TRONG);
        }

        AdminGenreEntity foundEntity = adminGenreRepository.findById(updateDTO.getId()).orElseThrow(
                () -> new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_ID_DA_CHON)
        );

        if(adminGenreRepository.isExistAndIdNot(updateDTO.getName(), updateDTO.getTitle(), updateDTO.getId())) {
            throw new BadRequestException(GENRE_ALREADY_EXISTS);
        }

        ObjectMapperUtils.map(updateDTO, foundEntity);
        if (foundEntity.getStatus().equals(AdminStatusEnum.SAVE_DRAF)) {
            foundEntity.setStatusSaveAndSendApprove();
        } else {
            entityManager.detach(foundEntity);
            // Nếu là trạng thái IsActiveEnum.ACTIVE thì chỉ được update trường newData
            // Chỉ update những cột được phép

            foundEntity.setNewData(null);
            String jsonData = Utils.convertToJson(foundEntity);
            foundEntity.setNewData(jsonData);
            // Giữ nguyên trạng thái tham số là CANCEL_APPROVE, Giữ nguyên trạng thái hoạt động hiện tại
            adminGenreRepository.updateNewData(jsonData, updateDTO.getId());
        }

        return foundEntity.getId();
    }

    @Override
    public String sendApproval(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
        Set<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toUnmodifiableSet());

        if (listIds.isEmpty()) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        // 2. Lấy danh sách GenreEntity từ DB
        List<AdminGenreEntity> genreList = adminGenreRepository.findAllById(listIds);
        if (ObjectUtils.isEmpty(genreList)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        List<AdminGenreEntity> listValid = new ArrayList<>();
        for (AdminGenreEntity e : genreList) {
            // Nếu không thỏa mãn điều kiện gửi duyệt, bỏ qua (giống continue)
            if (!ValidWorkFlowLv2.isSendApproveValid(e)) {
                throw new BadRequestException(Constants.TON_TAI_MOT_HOAC_NHIEU_THAM_SO_CO_TRANG_THAI_KHONG_THE_THUC_HIEN_THAO_TAC_NAY);
            }
            // Detach entity khỏi Persistence Context
            entityManager.detach(e);
            // Nếu có newData, chuyển đổi từ JSON sang entity mới
            String newData = e.getNewData();
            if (!Strings.isBlank(newData)) {
                e = Utils.convertJsonToObject(newData, AdminGenreEntity.class);
            }

            e.setStatus(AdminStatusEnum.WAIT_APPROVE);
            listValid.add(e);
        }


        adminGenreRepository.updateStatusByIds(AdminStatusEnum.WAIT_APPROVE, listIds);
        // 6. Ghi log hoạt động và cập nhật trạng thái cho các bản ghi hợp lệ
//        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.SEND_APPROVE, entitiesToSendApprove, GenreEntity::getId);
        // 7. Trả về kết quả
        return String.format(Constants.GUI_DUYET_THANH_CONG_PLACEHOLDER, listValid.size(), genreList.size());
    }

    @Override
    @Transactional
    public String approveParam(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
        if (adminIdsDto.getIds().isBlank()) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }
        List<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toUnmodifiableList());

        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<AdminGenreEntity> genreList =  adminGenreRepository.findAllById(listIds);

        if(ObjectUtils.isEmpty(genreList)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        if (!ValidWorkFlowLv2.isApproveListValid(genreList))
            throw new BadRequestException(Constants.TON_TAI_MOT_HOAC_NHIEU_THAM_SO_CO_TRANG_THAI_KHONG_THE_THUC_HIEN_THAO_TAC_NAY);

        List<AdminGenreEntity> entitiesToApprove = genreList.stream()
                .map(item -> {
                    AdminGenreEntity newDataObject = item.getNewData() != null
                            ? Utils.convertJsonToObject(item.getNewData(), AdminGenreEntity.class)
                            : item;
                    entityManager.detach(newDataObject);
                    newDataObject.setStatusApprove();
                    return newDataObject;
                }).filter(item -> !adminGenreRepository.isExistAndIdNot(item.getName(), item.getTitle(), item.getId()))
                .collect(Collectors.toList());

        if(entitiesToApprove.isEmpty()) {
            throw new BadRequestException(ALL_GENRE_ALREADY_EXISTS);
        }

//        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.APPROVE,
//                entitiesToApprove, GenreEntity::getId);

        return String.format(Constants.PHE_DUYET_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI, entitiesToApprove.size(), genreList.size());
    }

    @Override
    @Transactional
    public String cancelApprove(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
// TODO Auto-generated method stub
        Set<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toSet());
        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<AdminGenreEntity> genreList = adminGenreRepository.findAllById(listIds);
        if (ObjectUtils.isEmpty(genreList)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        List<AdminGenreEntity> listValid = new ArrayList<>();
        genreList.forEach(e -> {
            if(ValidWorkFlow.isCancelApproveValid(e)) {
                e.setNewData(null);
                e.setStatusCancelApproval();
                e.setNewData(Utils.convertToJson(e));
                listValid.add(e);
            }

        });
        if(listValid.isEmpty()) {
            throw new BadRequestException(Constants.KHONG_CO_BAN_GHI_NAO_DUOC_XU_LY);
        }

//        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.CANCEL_APPROVAL,genreList, GenreEntity::getId);
        return String.format(Constants.HUY_DUYET_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI, listValid.size(), genreList.size());
    }

    @Override
    @Transactional
    public String reject(AdminRejectDTO adminRejectDTO, HttpServletRequest filter) {
        Set<Long> listIds = Arrays.stream(adminRejectDTO.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toSet());
        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<AdminGenreEntity> genreList = adminGenreRepository.findAllById(listIds);
        if (ObjectUtils.isEmpty(genreList)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        List<AdminGenreEntity> listValid = new ArrayList<>();
        genreList.forEach(e -> {
            if(ValidWorkFlowLv2.isRejectValid(e)) {
                e.setStatus(AdminStatusEnum.REJECTED);
                if(e.getNewData() == null) {
                    e.setNewData(Utils.convertToJson(e));
                }
                listValid.add(e);
            }
        });
//        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.REJECT,genreList, GenreEntity::getId, rejectDTO.getLyDoTuChoi());

        return String.format(Constants.TU_CHOI_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI, listValid.size(), genreList.size());
    }

    @Override
    @Transactional
    public String delete(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
        List<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toUnmodifiableList());
        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<AdminGenreEntity> genreList = adminGenreRepository.findAllById(listIds);
        if (ObjectUtils.isEmpty(genreList)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        if (!ValidWorkFlow.isDeleteListValid(genreList)) {
            throw new BadRequestException(
                    Constants.TON_TAI_MOT_HOAC_NHIEU_THAM_SO_CO_TRANG_THAI_KHONG_THE_THUC_HIEN_THAO_TAC_NAY);
        }
        adminGenreRepository.deleteAllById(listIds);
//        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.DELETE,genreList, GenreEntity::getId);
        return String.format(Constants.XOA_THANH_CONG_XXX_BAN_GHI, listIds.size());
    }

    @Override
    public void exportExcel(AdminGenreFilterDTO filter, HttpServletRequest request, HttpServletResponse response) {

        try {
            Sort sort = Utils.generatedSort(filter.getSort());
            Specification<AdminGenreEntity> specification = this.getSearchSpecification(filter);
            List<AdminGenreEntity> list = adminGenreRepository.findAll(specification, sort);
            List<AdminGenreResponseDTO> result = list.stream().map(item -> {
                AdminGenreResponseDTO itemExcel = ObjectMapperUtils.map(item, AdminGenreResponseDTO.class);
                return itemExcel;
            }).collect(Collectors.toList());
            ExcelUtils.export(response, AdminGenreResponseDTO.class, result, "Thể loại phim", AdminParamEnum.GENRE.getValue());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    private Specification<AdminGenreEntity> getSearchSpecification(final AdminGenreFilterDTO filter) {
        return new Specification<>() {

            private static final long serialVersionUID = 6345534328548406667L;

            @Override
            @Nullable
            public Predicate toPredicate(Root<AdminGenreEntity> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (Strings.isNotBlank(filter.getIds())) {
                    List<String> ids = Arrays.asList(filter.getIds().split(","));
                    predicates.add(root.get("id").in(ids));
                }

                if (!ObjectUtils.isEmpty(filter.getStatus()) && !filter.getStatus().isBlank()) {
                    Set<Integer> status = Arrays.stream(filter.getStatus().split(",")).map(Integer::valueOf).collect(Collectors.toSet());
                    predicates.add(root.get("status").in(status));
                }

                if (!ObjectUtils.isEmpty(filter.getIsActive()) && !filter.getIsActive().isBlank()) {
                    Set<Integer> isActive = Arrays.stream(filter.getIsActive().split(",")).map(Integer::valueOf)
                            .collect(Collectors.toSet());
                    predicates.add(root.get("isActive").in(isActive));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
    }
}
