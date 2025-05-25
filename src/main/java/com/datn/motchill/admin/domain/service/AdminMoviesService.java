package com.datn.motchill.admin.domain.service;

import com.datn.motchill.admin.presentation.dto.AdminIdsDTO;
import com.datn.motchill.admin.presentation.dto.AdminRejectDTO;
import com.datn.motchill.admin.presentation.dto.movies.AdminMovieResponseDTO;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesCreateDto;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesFilterDto;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminMoviesService {
    AdminMovieResponseDTO findById(Long id);

    Page<AdminMovieResponseDTO> search(AdminMoviesFilterDto filter);

    String uploadFile(MultipartFile file, String type) throws IOException;

    Long saveDraftCollect(AdminMoviesCreateDto createDTO, MultipartFile thumb, HttpServletRequest request);

    Long saveDraft(AdminMoviesCreateDto createDto, final HttpServletRequest request);


    Long saveAndApprove(AdminMoviesCreateDto createDto, final HttpServletRequest request);

    Long saveAndSendApproval(AdminMoviesCreateDto createDto, final HttpServletRequest request);


//    Long updateDraft(AdminMoviesUpdateDto updateDto, final HttpServletRequest request);
//
//    Long updateAndSendApproval(AdminMoviesUpdateDto updateDto, final HttpServletRequest request);
//
//    String sendApproval(AdminIdsDTO adminIdsDto, final HttpServletRequest request);
//
//    String approveParam(AdminIdsDTO adminIdsDto, final HttpServletRequest request);
//
//    String cancelApprove(AdminIdsDTO adminIdsDto, final HttpServletRequest request);
//
//    String reject(AdminRejectDTO adminRejectDTO, final HttpServletRequest request);
//
//    String delete(AdminIdsDTO adminIdsDto, final HttpServletRequest request);
//
////    void importExcel(MultipartFile file, final HttpServletRequest request, HttpServletResponse response);
//
//    void exportExcel(AdminMoviesFilterDto filter, final HttpServletRequest request, final HttpServletResponse response);

}
