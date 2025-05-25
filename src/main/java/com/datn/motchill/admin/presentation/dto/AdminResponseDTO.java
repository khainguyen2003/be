package com.datn.motchill.admin.presentation.dto;

import com.datn.motchill.admin.presentation.enums.AdminIsActiveEnum;
import com.datn.motchill.admin.presentation.enums.AdminIsDisplayEnum;
import com.datn.motchill.admin.presentation.enums.AdminStatusEnum;
import com.datn.motchill.shared.annotation.ExcelColumn;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Size;

public class AdminResponseDTO {
    @ExcelColumn("Thạng thái tham số")
    @Convert(converter = AdminStatusEnum.StatusEnumConverter.class)
    private AdminStatusEnum status;

    @ExcelColumn("Thạng thái on/off")
    @Convert(converter = AdminIsDisplayEnum.IsDisplayEnumConverter.class)
    private AdminIsDisplayEnum isDisplay;

    @ExcelColumn("Thạng thái")
    @Convert(converter = AdminIsActiveEnum.IsActiveEnumConverter.class)
    private AdminIsActiveEnum isActive;

    @Size(max = 4000, message = "")
    private String newData;
}
