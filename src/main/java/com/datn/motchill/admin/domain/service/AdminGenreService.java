package com.datn.motchill.admin.domain.service;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminGenreEntity;
import com.datn.motchill.admin.presentation.dto.AdminIdsDTO;
import com.datn.motchill.admin.presentation.dto.AdminOptionDTO;
import com.datn.motchill.admin.presentation.dto.AdminRejectDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreCreateDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreFilterDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreResponseDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreUpdateDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface AdminGenreService {

    AdminGenreResponseDTO getGenreById(Long id);

    Set<AdminOptionDTO> getActiveGenreOptions(AdminGenreFilterDTO filterDTO);

    void validateActiveGenres(String genreCodes, AdminGenreFilterDTO filter);
    List<AdminGenreEntity> getActiveGenreEntityByListGenreCode(Set<String> genreCodes, AdminGenreFilterDTO filter);

    Page<AdminGenreResponseDTO> search(AdminGenreFilterDTO filter);

    Long saveDraft(AdminGenreCreateDTO componentCreateDTO, final HttpServletRequest request);

    Long saveAndApprove(AdminGenreCreateDTO componentCreateDTO, final HttpServletRequest request);

    Long saveAndSendApproval(AdminGenreCreateDTO componentCreateDTO, final HttpServletRequest request);


    Long updateDraft(AdminGenreUpdateDTO componentUpdateDTO, final HttpServletRequest request);

    Long updateAndSendApproval(AdminGenreUpdateDTO componentUpdateDTO, final HttpServletRequest request);

    String sendApproval(AdminIdsDTO adminIdsDto, final HttpServletRequest request);

    String approveParam(AdminIdsDTO adminIdsDto, final HttpServletRequest request);

    String cancelApprove(AdminIdsDTO adminIdsDto, final HttpServletRequest request);

    String reject(AdminRejectDTO adminRejectDTO, final HttpServletRequest request);

    String delete(AdminIdsDTO adminIdsDto, final HttpServletRequest request);

//    void importExcel(MultipartFile file, final HttpServletRequest request, HttpServletResponse response);

    void exportExcel(AdminGenreFilterDTO filter, final HttpServletRequest request, final HttpServletResponse response);

}
