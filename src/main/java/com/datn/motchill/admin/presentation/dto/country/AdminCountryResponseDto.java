package com.datn.motchill.admin.presentation.dto.country;

import com.datn.motchill.admin.presentation.dto.AdminResponseDTO;
import com.datn.motchill.shared.annotation.ColumnMapping;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.admin.infrastructure.persistence.entity.AdminCountryEntity}
 */
@Data
public class AdminCountryResponseDto extends AdminResponseDTO implements Serializable {
    @ColumnMapping("id")
    private Long id;

    @ColumnMapping("ISO_CODE")
    private String isoCode;

    @ColumnMapping("THREE_ISO_CODE")
    private String threeIsoCode;

    @ColumnMapping("COUNTRY_NAME")
    private String countryName;

    @ColumnMapping("country_full_name")
    private String countryFullName;
}