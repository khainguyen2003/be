package com.datn.motchill.admin.presentation.rest;

import com.datn.motchill.admin.common.utils.ObjectMapperUtils;
import com.datn.motchill.admin.infrastructure.persistence.entity.AdminMoviesEntity;
import com.datn.motchill.admin.presentation.dto.AdminBaseResponse;
import com.datn.motchill.admin.presentation.dto.AdminIdsDTO;
import com.datn.motchill.admin.presentation.dto.AdminRejectDTO;
import com.datn.motchill.admin.presentation.dto.UploadResponseDTO;
import com.datn.motchill.admin.presentation.dto.movies.AdminMovieResponseDTO;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesCreateDto;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesFilterDto;
import com.datn.motchill.admin.domain.service.AdminMoviesService;
import com.datn.motchill.admin.common.utils.Constants;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesUpdateDto;
import com.datn.motchill.customer.presentation.dto.IdsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.API_PATH.API_ADMIN_PATH.ADMIN_MOVIES)
public class AdminMoviesController {
    private final AdminMoviesService adminMoviesService;

    @PostMapping("/{id}")
    public ResponseEntity<AdminBaseResponse> getById(@PathVariable("id") Long id, HttpServletRequest request){
        AdminBaseResponse baseResponse = new AdminBaseResponse().success(adminMoviesService.findById(id));
        return ResponseEntity.ok(baseResponse);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/search")
    public ResponseEntity<AdminBaseResponse> search(@RequestBody AdminMoviesFilterDto filterDTO) {
        Page<AdminMovieResponseDTO> pmhComponents = this.adminMoviesService.search(filterDTO);
        AdminBaseResponse response = new AdminBaseResponse().success(pmhComponents);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if (file.getSize() > 100 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("Video size exceeds 100MB limit");
        }

        String fileUrl = this.adminMoviesService.uploadFile(file, "videos");
        return ResponseEntity.ok(new UploadResponseDTO(fileUrl));
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping(value =  "/save-draft-collect", consumes = {"multipart/form-data"})
    public ResponseEntity<AdminBaseResponse> saveDraftCollect(
        @RequestParam("movie") String movieJson,
        @RequestParam("thumb") MultipartFile thumb,
        HttpServletRequest request
    ) {
        AdminMoviesCreateDto movieDto = ObjectMapperUtils.map(movieJson, AdminMoviesCreateDto.class);
        Long id = adminMoviesService.saveDraftCollect(movieDto, thumb, request);
        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/save-draft")
    public ResponseEntity<AdminBaseResponse> saveDraft(@Valid @RequestBody AdminMoviesCreateDto createDto, HttpServletRequest request) {
        Long id = adminMoviesService.saveDraft(createDto, request);
        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/save-approve")
    public ResponseEntity<AdminBaseResponse> saveAndApprove(@Valid @RequestBody AdminMoviesCreateDto createDto, HttpServletRequest request) {
        Long id = adminMoviesService.saveAndApprove(createDto, request);
        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/save-send-approve")
    public ResponseEntity<AdminBaseResponse> saveAndPush(@Valid @RequestBody AdminMoviesCreateDto createDto, HttpServletRequest request) {
        Long id = adminMoviesService.saveAndSendApproval(createDto, request);
        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
//    @PostMapping("/update-draft")
//    public ResponseEntity<AdminBaseResponse> updateDraft(@Valid @RequestBody AdminMoviesUpdateDto updateDto, HttpServletRequest request) {
//
//        Long id = adminMoviesService.updateDraft(updateDto, request);
//
//        AdminBaseResponse response = new AdminBaseResponse().success(id);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    /**
//     * Author: khainv_llq<br/>
//     * Since: 03/03/2025 10:37 AM<br/>
//     * Description:
//     */
//    @PostMapping("/update-send-approve")
//    public ResponseEntity<AdminBaseResponse> updateAndSendApproval(@Valid @RequestBody AdminMoviesUpdateDto updateDto, HttpServletRequest request) {
//        Long id = adminMoviesService.updateAndSendApproval(updateDto, request);
//
//        AdminBaseResponse response = new AdminBaseResponse().success(id);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    /**
//     * Author: khainv_llq<br/>
//     * Since: 03/03/2025 10:37 AM<br/>
//     * Description:
//     */
//    @PostMapping("/send-approve")
//    public ResponseEntity<AdminBaseResponse> sendApprove(@Valid @RequestBody AdminIdsDTO idsDto, HttpServletRequest request) {
//        String sendApprovalResponse = adminMoviesService.sendApproval(idsDto, request);
//
//        AdminBaseResponse response = new AdminBaseResponse().success(sendApprovalResponse);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    /**
//     * Author: khainv_llq<br/>
//     * Since: 03/03/2025 10:37 AM<br/>
//     * Description:
//     */
//    @PostMapping("/approve")
//    public ResponseEntity<AdminBaseResponse> approve(@Valid @RequestBody AdminIdsDTO idsDto, HttpServletRequest request) {
//        String approveResponse = adminMoviesService.approveParam(idsDto, request);
//        AdminBaseResponse response = new AdminBaseResponse().success(approveResponse);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    /**
//     * Author: khainv_llq<br/>
//     * Since: 03/03/2025 10:37 AM<br/>
//     * Description:
//     */
//    @PostMapping("/cancel-approve")
//    public ResponseEntity<AdminBaseResponse> cancelApproval(@Valid @RequestBody AdminIdsDTO idsDto, HttpServletRequest request) {
//
//        String cancelApproveResponse = adminMoviesService.cancelApprove(idsDto, request);
//
//        AdminBaseResponse response = new AdminBaseResponse().success(cancelApproveResponse);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    /**
//     * Author: khainv_llq<br/>
//     * Since: 03/03/2025 10:37 AM<br/>
//     * Description:
//     */
//    @PostMapping("/reject")
//    public ResponseEntity<AdminBaseResponse> reject(@Valid @RequestBody AdminRejectDTO rejectDTO, HttpServletRequest request) {
//
//        String rejectResponse = adminMoviesService.reject(rejectDTO, request);
//
//        AdminBaseResponse response = new AdminBaseResponse().success(rejectResponse);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    /**
//     * Author: khainv_llq<br/>
//     * Since: 03/03/2025 10:37 AM<br/>
//     * Description:
//     */
//    @PostMapping("/delete")
//    public ResponseEntity<AdminBaseResponse> delete(@Valid @RequestBody AdminIdsDTO idsDto, HttpServletRequest request) {
//        String responseDelete = adminMoviesService.delete(idsDto, request);
//
//        AdminBaseResponse response = new AdminBaseResponse().success(responseDelete);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    /**
//     * Author: khainv_llq<br/>
//     * Since: 03/03/2025 10:37 AM<br/>
//     * Description:
//     */
//    @PostMapping("/export")
//    public void exportExcel(final HttpServletRequest request, HttpServletResponse response, @RequestBody AdminMoviesFilterDto filter) {
//        adminMoviesService.exportExcel( filter, request, response);
//    }


}
