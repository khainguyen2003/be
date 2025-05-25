package com.datn.motchill.admin.presentation.dto;

import com.datn.motchill.admin.common.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminIdsDTO {

    @NotBlank(message = Constants.ID_KHONG_DUOC_DE_TRONG)
    private String ids;
}
