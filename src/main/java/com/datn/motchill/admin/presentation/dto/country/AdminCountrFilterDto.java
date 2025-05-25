package com.datn.motchill.admin.presentation.dto.country;

import com.datn.motchill.admin.presentation.dto.AdminPageFilterDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.admin.infrastructure.persistence.entity.AdminCountryEntity}
 */
@Data
public class AdminCountrFilterDto extends AdminPageFilterDTO {
    @NotBlank(message = "Tên quốc gia không được để trống")
    @Size(max = 50, message = "Tên quốc gia tối đa 50 ký tự")
    private String countryName;

    @Size(max = 255, message = "Tên đầy đủ quốc gia tối đa 255 ký tự")
    private String countryFullName;
}