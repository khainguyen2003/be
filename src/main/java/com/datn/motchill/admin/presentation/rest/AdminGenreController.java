package com.datn.motchill.admin.presentation.rest;


import com.datn.motchill.admin.domain.service.AdminGenreService;
import com.datn.motchill.admin.presentation.dto.AdminBaseResponse;
import com.datn.motchill.admin.presentation.dto.AdminIdsDTO;
import com.datn.motchill.admin.presentation.dto.AdminOptionDTO;
import com.datn.motchill.admin.presentation.dto.AdminRejectDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreCreateDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreFilterDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreResponseDTO;
import com.datn.motchill.admin.presentation.dto.genre.AdminGenreUpdateDTO;
import com.datn.motchill.admin.common.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(Constants.API_PATH.API_ADMIN_PATH.ADMIN_GENRE)
@RequiredArgsConstructor
public class AdminGenreController {
    private final AdminGenreService adminGenreService;
    private final String PARAM_CODE = "GENRE";

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/get-active-component-options")
    public ResponseEntity<AdminBaseResponse> getActiveGenreOptions(@RequestBody AdminGenreFilterDTO filterDTO) {
        Set<AdminOptionDTO> genreOptions = adminGenreService.getActiveGenreOptions(filterDTO);
        AdminBaseResponse response = new AdminBaseResponse().success(genreOptions);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/{id}")
    public ResponseEntity<AdminBaseResponse> getById(@PathVariable("id") Long id, HttpServletRequest request){
        AdminBaseResponse adminBaseResponse = new AdminBaseResponse().success(adminGenreService.getGenreById(id));
        return ResponseEntity.ok(adminBaseResponse);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/search")
    public ResponseEntity<AdminBaseResponse> search(@RequestBody AdminGenreFilterDTO filterDTO) {
        Page<AdminGenreResponseDTO> genres = this.adminGenreService.search(filterDTO);
        AdminBaseResponse response = new AdminBaseResponse().success(genres);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/save-draft")
    public ResponseEntity<AdminBaseResponse> saveDraft(@Valid @RequestBody AdminGenreCreateDTO createDTO, HttpServletRequest request) {
        Long id = adminGenreService.saveDraft(createDTO, request);
        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/save-approve")
    public ResponseEntity<AdminBaseResponse> saveAndApprove(@Valid @RequestBody AdminGenreCreateDTO createDTO, HttpServletRequest request) {
        Long id = adminGenreService.saveAndApprove(createDTO, request);
        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/save-send-approve")
    public ResponseEntity<AdminBaseResponse> saveAndPush(@Valid @RequestBody AdminGenreCreateDTO createDTO, HttpServletRequest request) {
        Long id = adminGenreService.saveAndSendApproval(createDTO, request);
        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/update-draft")
    public ResponseEntity<AdminBaseResponse> updateDraft(@Valid @RequestBody AdminGenreUpdateDTO updateDTO, HttpServletRequest request) {

        Long id = adminGenreService.updateDraft(updateDTO, request);

        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/update-send-approve")
    public ResponseEntity<AdminBaseResponse> updateAndSendApproval(@Valid @RequestBody AdminGenreUpdateDTO updateDTO, HttpServletRequest request) {
        Long id = adminGenreService.updateAndSendApproval(updateDTO, request);

        AdminBaseResponse response = new AdminBaseResponse().success(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/send-approve")
    public ResponseEntity<AdminBaseResponse> sendApprove(@Valid @RequestBody AdminIdsDTO adminIdsDto, HttpServletRequest request) {
        String sendApprovalResponse = adminGenreService.sendApproval(adminIdsDto, request);

        AdminBaseResponse response = new AdminBaseResponse().success(sendApprovalResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/approve")
    public ResponseEntity<AdminBaseResponse> approve(@Valid @RequestBody AdminIdsDTO adminIdsDto, HttpServletRequest request) {
        String approveResponse = adminGenreService.approveParam(adminIdsDto, request);
        AdminBaseResponse response = new AdminBaseResponse().success(approveResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/cancel-approve")
    public ResponseEntity<AdminBaseResponse> cancelApproval(@Valid @RequestBody AdminIdsDTO adminIdsDto, HttpServletRequest request) {

        String cancelApproveResponse = adminGenreService.cancelApprove(adminIdsDto, request);

        AdminBaseResponse response = new AdminBaseResponse().success(cancelApproveResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/reject")
    public ResponseEntity<AdminBaseResponse> reject(@Valid @RequestBody AdminRejectDTO adminRejectDTO, HttpServletRequest request) {

        String rejectResponse = adminGenreService.reject(adminRejectDTO, request);

        AdminBaseResponse response = new AdminBaseResponse().success(rejectResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/delete")
    public ResponseEntity<AdminBaseResponse> delete(@Valid @RequestBody AdminIdsDTO adminIdsDto, HttpServletRequest request) {
        String responseDelete = adminGenreService.delete(adminIdsDto, request);

        AdminBaseResponse response = new AdminBaseResponse().success(responseDelete);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Author: khainv_llq<br/>
     * Since: 03/03/2025 10:37 AM<br/>
     * Description:
     */
    @PostMapping("/export")
    public void exportExcel(final HttpServletRequest request, HttpServletResponse response, @RequestBody AdminGenreFilterDTO filter) {
        adminGenreService.exportExcel( filter, request, response);
    }
}
