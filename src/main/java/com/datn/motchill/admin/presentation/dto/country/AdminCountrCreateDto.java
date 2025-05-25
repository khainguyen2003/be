package com.datn.motchill.admin.presentation.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.admin.infrastructure.persistence.entity.AdminCountryEntity}
 */
@Data
public class AdminCountrCreateDto implements Serializable {
    @NotBlank(message = "Mã ISO không được để trống")
    @Size(min = 2, max = 2, message = "Mã ISO phải gồm 2 ký tự")
    private String isoCode;

    @Size(min = 3, max = 3, message = "Mã ISO 3 ký tự phải gồm 3 ký tự")
    private String threeIsoCode;

    @NotBlank(message = "Tên quốc gia không được để trống")
    @Size(max = 50, message = "Tên quốc gia tối đa 50 ký tự")
    private String countryName;

    @Size(max = 255, message = "Tên đầy đủ quốc gia tối đa 255 ký tự")
    private String countryFullName;
}