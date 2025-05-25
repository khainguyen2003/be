package com.datn.motchill.admin.domain.service.impl;

import com.datn.motchill.admin.common.exceptions.BadRequestException;
import com.datn.motchill.admin.common.utils.Constants;
import com.datn.motchill.admin.common.utils.ObjectMapperUtils;
import com.datn.motchill.admin.common.utils.Utils;
import com.datn.motchill.admin.domain.service.AdminMoviesService;
import com.datn.motchill.admin.infrastructure.persistence.entity.AdminMoviesEntity;
import com.datn.motchill.admin.infrastructure.persistence.repository.AdminMoviesRepository;
import com.datn.motchill.admin.infrastructure.persistence.repository.custom.AdminMovieCustomRepository;
import com.datn.motchill.admin.presentation.dto.movies.AdminMovieResponseDTO;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesCreateDto;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesFilterDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminMoviesServiceImpl implements AdminMoviesService {
    private final AdminMoviesRepository adminMoviesRepository;
    private final AdminMovieCustomRepository adminMovieCustomRepository;

    private static final String UPLOAD_IMAGE_DIR = "images/";
    private static final String UPLOAD_VIDEO_DIR = "videos/";

    private final ObjectMapper mapper;

    private final String MOVIES_ALREADY_EXISTS = "Phim đã tồn tại!";
    private final String ALL_MOVIES_ALREADY_EXISTS = "Tất cả phim được chọn đều đã tồn tại";

    @Override
    public AdminMovieResponseDTO findById(Long id) {
        return mapper.convertValue(
                adminMoviesRepository.findById(id).orElseThrow(() -> new BadRequestException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON))
        , AdminMovieResponseDTO.class);
    }


    @Override
    public Page<AdminMovieResponseDTO> search(AdminMoviesFilterDto filter) {
        Sort sort = Utils.generatedSort(filter.getSort());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit(), sort);
         Page<AdminMovieResponseDTO> pageMovie = adminMovieCustomRepository.findAll(filter, pageable);
        return pageMovie;
    }

    @Override
    public String uploadFile(MultipartFile file, String type) throws IOException {
        String dir = type.equals("images") ? UPLOAD_IMAGE_DIR : UPLOAD_VIDEO_DIR;
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(dir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return "/" + type + "/" + fileName;
    }

    @Override
    public Long saveDraftCollect(AdminMoviesCreateDto createDTO, MultipartFile thumb, HttpServletRequest request) {
//        if(adminMoviesRepository.isExists(createDTO.getName())) {
//            throw new BadRequestException(MOVIES_ALREADY_EXISTS);
//        }

        if (thumb.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Thumbnail size exceeds 5MB limit");
        }
        String thumbContentType = thumb.getContentType();
        if (!List.of("image/jpeg", "image/png", "image/gif").contains(thumbContentType)) {
            throw new IllegalArgumentException("File không đúng định dạng hình ảnh");
        }
        try {
            String thumbUrl = this.uploadFile(thumb, "images");
            AdminMoviesEntity newEntity = ObjectMapperUtils.map(createDTO, AdminMoviesEntity.class);
            newEntity.setThumbUrl(thumbUrl);
            newEntity.setStatusSaveDraftColected();

            AdminMoviesEntity entityInDB = adminMoviesRepository.save(newEntity);
            // lưu log họat động
            return entityInDB.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Long saveDraft(AdminMoviesCreateDto createDTO, HttpServletRequest filter) {

        // check thể loại phim, quốc gia,

        if(adminMoviesRepository.isExists(createDTO.getName())) {
            throw new BadRequestException(MOVIES_ALREADY_EXISTS);
        }

        AdminMoviesEntity newEntity = ObjectMapperUtils.map(createDTO, AdminMoviesEntity.class);
        newEntity.setStatusSaveDraft();

        AdminMoviesEntity entityInDB = adminMoviesRepository.save(newEntity);
        // lưu log họat động
        return entityInDB.getId();
    }

    @Override
    public Long saveAndApprove(AdminMoviesCreateDto createDTO, HttpServletRequest filter) {
        if(adminMoviesRepository.isExists(createDTO.getName())) {
            throw new BadRequestException(MOVIES_ALREADY_EXISTS);
        }

        AdminMoviesEntity newEntity = ObjectMapperUtils.map(createDTO, AdminMoviesEntity.class);
        newEntity.setStatusSaveAndApprove();
        AdminMoviesEntity entityInDB = adminMoviesRepository.save(newEntity);


        return entityInDB.getId();
    }

    @Override
    public Long saveAndSendApproval(AdminMoviesCreateDto createDTO, HttpServletRequest filter) {
        if(adminMoviesRepository.isExists(createDTO.getName())) {
            throw new BadRequestException(MOVIES_ALREADY_EXISTS);
        }

        AdminMoviesEntity newEntity = ObjectMapperUtils.map(createDTO, AdminMoviesEntity.class);
        newEntity.setStatusSaveAndSendApprove();
        AdminMoviesEntity entityInDB = adminMoviesRepository.save(newEntity);

        return entityInDB.getId();
    }

//    @Override
//    public Long updateDraft(AdminMoviesUpdateDto updateDTO, HttpServletRequest filter) {
//        if(updateDTO.getId() == null) {
//            throw new BadRequestException(Constants.ID_KHONG_DUOC_DE_TRONG);
//        }
//
//        AdminMoviesEntity foundEntity = adminMoviesRepository.findById(updateDTO.getId()).orElseThrow(
//                () -> new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_ID_DA_CHON)
//        );
//
//        if(adminMoviesRepository.isExistsAndIdNot(updateDTO.getName(), updateDTO.getTitle(), updateDTO.getId())) {
//            throw new BadRequestException(MOVIES_ALREADY_EXISTS);
//        }
//
//        ObjectMapperUtils.map(updateDTO, foundEntity);
//        if (foundEntity.getStatus().equals(AdminStatusEnum.SAVE_DRAF)) {
//            foundEntity.setStatusSaveDraft();
//        } else {
//            entityManager.detach(foundEntity);
//            // Nếu là trạng thái IsActiveEnum.ACTIVE thì chỉ được update trường newData
//            // Chỉ update những cột được phép
//
//            foundEntity.setNewData(null);
//            String jsonData = Utils.convertToJson(foundEntity);
//            foundEntity.setNewData(jsonData);
//            // Giữ nguyên trạng thái tham số là CANCEL_APPROVE, Giữ nguyên trạng thái hoạt động hiện tại
//            adminMoviesRepository.updateNewData(jsonData, updateDTO.getId());
//        }
//
//        return foundEntity.getId();
//    }
//
//    @Override
//    public Long updateAndSendApproval(AdminMoviesUpdateDto updateDTO, HttpServletRequest filter) {
//        if(updateDTO.getId() == null) {
//            throw new BadRequestException(Constants.ID_KHONG_DUOC_DE_TRONG);
//        }
//
//        AdminMoviesEntity foundEntity = adminMoviesRepository.findById(updateDTO.getId()).orElseThrow(
//                () -> new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_ID_DA_CHON)
//        );
//
//        if(adminMoviesRepository.isExistAndIdNot(updateDTO.getName(), updateDTO.getTitle(), updateDTO.getId())) {
//            throw new BadRequestException(MOVIES_ALREADY_EXISTS);
//        }
//
//        ObjectMapperUtils.map(updateDTO, foundEntity);
//        if (foundEntity.getStatus().equals(AdminStatusEnum.SAVE_DRAF)) {
//            foundEntity.setStatusSaveAndSendApprove();
//        } else {
//            entityManager.detach(foundEntity);
//            // Nếu là trạng thái IsActiveEnum.ACTIVE thì chỉ được update trường newData
//            // Chỉ update những cột được phép
//
//            foundEntity.setNewData(null);
//            String jsonData = Utils.convertToJson(foundEntity);
//            foundEntity.setNewData(jsonData);
//            // Giữ nguyên trạng thái tham số là CANCEL_APPROVE, Giữ nguyên trạng thái hoạt động hiện tại
//            adminMoviesRepository.updateNewData(jsonData, updateDTO.getId());
//        }
//
//        return foundEntity.getId();
//    }
//
//    @Override
//    public String sendApproval(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
//        Set<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(","))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .map(Long::parseLong)
//                .collect(Collectors.toUnmodifiableSet());
//
//        if (listIds.isEmpty()) {
//            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
//        }
//
//        // 2. Lấy danh sách GenreEntity từ DB
//        List<AdminMoviesEntity> genreList = adminMoviesRepository.findAllById(listIds);
//        if (ObjectUtils.isEmpty(genreList)) {
//            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
//        }
//
//        List<AdminMoviesEntity> listValid = new ArrayList<>();
//        for (AdminMoviesEntity e : genreList) {
//            // Nếu không thỏa mãn điều kiện gửi duyệt, bỏ qua (giống continue)
//            if (!ValidWorkFlowLv2.isSendApproveValid(e)) {
//                throw new BadRequestException(Constants.TON_TAI_MOT_HOAC_NHIEU_THAM_SO_CO_TRANG_THAI_KHONG_THE_THUC_HIEN_THAO_TAC_NAY);
//            }
//            // Detach entity khỏi Persistence Context
//            entityManager.detach(e);
//            // Nếu có newData, chuyển đổi từ JSON sang entity mới
//            String newData = e.getNewData();
//            if (!Strings.isBlank(newData)) {
//                e = Utils.convertJsonToObject(newData, AdminMoviesEntity.class);
//            }
//
//            e.setStatus(AdminStatusEnum.WAIT_APPROVE);
//            listValid.add(e);
//        }
//
//
//        adminMoviesRepository.updateStatusByIds(AdminStatusEnum.WAIT_APPROVE, listIds);
//        // 6. Ghi log hoạt động và cập nhật trạng thái cho các bản ghi hợp lệ
////        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.SEND_APPROVE, entitiesToSendApprove, GenreEntity::getId);
//        // 7. Trả về kết quả
//        return String.format(Constants.GUI_DUYET_THANH_CONG_PLACEHOLDER, listValid.size(), genreList.size());
//    }
//
//    @Override
//    @Transactional
//    public String approveParam(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
//        if (adminIdsDto.getIds().isBlank()) {
//            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
//        }
//        List<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(",")).map(Long::parseLong)
//                .collect(Collectors.toUnmodifiableList());
//
//        if (ObjectUtils.isEmpty(listIds)) {
//            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
//        }
//
//        List<AdminMoviesEntity> genreList =  adminMoviesRepository.findAllById(listIds);
//
//        if(ObjectUtils.isEmpty(genreList)) {
//            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
//        }
//
//        if (!ValidWorkFlowLv2.isApproveListValid(genreList))
//            throw new BadRequestException(Constants.TON_TAI_MOT_HOAC_NHIEU_THAM_SO_CO_TRANG_THAI_KHONG_THE_THUC_HIEN_THAO_TAC_NAY);
//
//        List<AdminMoviesEntity> entitiesToApprove = genreList.stream()
//                .map(item -> {
//                    AdminMoviesEntity newDataObject = item.getNewData() != null
//                            ? Utils.convertJsonToObject(item.getNewData(), AdminMoviesEntity.class)
//                            : item;
//                    entityManager.detach(newDataObject);
//                    newDataObject.setStatusApprove();
//                    return newDataObject;
//                }).filter(item -> !adminMoviesRepository.isExistAndIdNot(item.getName(), item.getTitle(), item.getId()))
//                .collect(Collectors.toList());
//
//        if(entitiesToApprove.isEmpty()) {
//            throw new BadRequestException(ALL_MOVIES_ALREADY_EXISTS);
//        }
//
////        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.APPROVE,
////                entitiesToApprove, GenreEntity::getId);
//
//        return String.format(Constants.PHE_DUYET_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI, entitiesToApprove.size(), genreList.size());
//    }
//
//    @Override
//    @Transactional
//    public String cancelApprove(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
//// TODO Auto-generated method stub
//        Set<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(",")).map(Long::parseLong)
//                .collect(Collectors.toSet());
//        if (ObjectUtils.isEmpty(listIds)) {
//            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
//        }
//
//        List<AdminMoviesEntity> genreList = adminMoviesRepository.findAllById(listIds);
//        if (ObjectUtils.isEmpty(genreList)) {
//            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
//        }
//
//        List<AdminMoviesEntity> listValid = new ArrayList<>();
//        genreList.forEach(e -> {
//            if(ValidWorkFlow.isCancelApproveValid(e)) {
//                e.setNewData(null);
//                e.setStatusCancelApproval();
//                e.setNewData(Utils.convertToJson(e));
//                listValid.add(e);
//            }
//
//        });
//        if(listValid.isEmpty()) {
//            throw new BadRequestException(Constants.KHONG_CO_BAN_GHI_NAO_DUOC_XU_LY);
//        }
//
////        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.CANCEL_APPROVAL,genreList, GenreEntity::getId);
//        return String.format(Constants.HUY_DUYET_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI, listValid.size(), genreList.size());
//    }
//
//    @Override
//    @Transactional
//    public String reject(AdminRejectDTO adminRejectDTO, HttpServletRequest filter) {
//        Set<Long> listIds = Arrays.stream(adminRejectDTO.getIds().split(",")).map(Long::parseLong)
//                .collect(Collectors.toSet());
//        if (ObjectUtils.isEmpty(listIds)) {
//            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
//        }
//
//        List<AdminMoviesEntity> genreList = adminMoviesRepository.findAllById(listIds);
//        if (ObjectUtils.isEmpty(genreList)) {
//            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
//        }
//
//        List<AdminMoviesEntity> listValid = new ArrayList<>();
//        genreList.forEach(e -> {
//            if(ValidWorkFlowLv2.isRejectValid(e)) {
//                e.setStatus(AdminStatusEnum.REJECTED);
//                if(e.getNewData() == null) {
//                    e.setNewData(Utils.convertToJson(e));
//                }
//                listValid.add(e);
//            }
//        });
////        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.REJECT,genreList, GenreEntity::getId, rejectDTO.getLyDoTuChoi());
//
//        return String.format(Constants.TU_CHOI_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI, listValid.size(), genreList.size());
//    }
//
//    @Override
//    @Transactional
//    public String delete(AdminIdsDTO adminIdsDto, HttpServletRequest filter) {
//        List<Long> listIds = Arrays.stream(adminIdsDto.getIds().split(",")).map(Long::parseLong)
//                .collect(Collectors.toUnmodifiableList());
//        if (ObjectUtils.isEmpty(listIds)) {
//            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
//        }
//
//        List<AdminMoviesEntity> genreList = adminMoviesRepository.findAllById(listIds);
//        if (ObjectUtils.isEmpty(genreList)) {
//            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
//        }
//
//        if (!ValidWorkFlow.isDeleteListValid(genreList)) {
//            throw new BadRequestException(
//                    Constants.TON_TAI_MOT_HOAC_NHIEU_THAM_SO_CO_TRANG_THAI_KHONG_THE_THUC_HIEN_THAO_TAC_NAY);
//        }
//        adminMoviesRepository.deleteAllById(listIds);
////        quanLyHoatDongService.saveAll(filter, ParamEnum.COMPONENTS, ActionEnum.DELETE,genreList, GenreEntity::getId);
//        return String.format(Constants.XOA_THANH_CONG_XXX_BAN_GHI, listIds.size());
//    }
//
//    @Override
//    public void exportExcel(AdminMoviesFilterDto filter, HttpServletRequest request, HttpServletResponse response) {
//
//        try {
//            List<AdminMovieResponseDTO> list = adminMovieCustomRepository.findAll(filter);
//            List<AdminGenreResponseDTO> result = list.stream().map(item -> {
//                AdminGenreResponseDTO itemExcel = ObjectMapperUtils.map(item, AdminGenreResponseDTO.class);
//                return itemExcel;
//            }).collect(Collectors.toList());
//            ExcelUtils.export(response, AdminGenreResponseDTO.class, result, "Thể loại phim", AdminParamEnum.GENRE.getValue());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            throw new RuntimeException(e);
//        }
//    }

    private Specification<AdminMoviesEntity> getSearchOptionsSpecification(final AdminMoviesFilterDto filterDto) {
        return new Specification<>() {

            private static final long serialVersionUID = 6345534328548406667L;

            @Override
            @Nullable
            public Predicate toPredicate(Root<AdminMoviesEntity> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();


                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
