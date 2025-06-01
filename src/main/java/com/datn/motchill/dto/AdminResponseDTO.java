package com.datn.motchill.dto;

import com.datn.motchill.enums.IsActiveEnum;
import com.datn.motchill.enums.IsDisplayEnum;
import com.datn.motchill.enums.StatusEnum;
import com.datn.motchill.shared.annotation.ExcelColumn;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Size;

public class AdminResponseDTO {
    @ExcelColumn("Thạng thái tham số")
    @Convert(converter = StatusEnum.StatusEnumConverter.class)
    private StatusEnum status;

    @ExcelColumn("Thạng thái on/off")
    @Convert(converter = IsDisplayEnum.IsDisplayEnumConverter.class)
    private IsDisplayEnum isDisplay;

    @ExcelColumn("Thạng thái")
    @Convert(converter = IsActiveEnum.IsActiveEnumConverter.class)
    private IsActiveEnum isActive;

    @Size(max = 4000, message = "")
    private String newData;
}
