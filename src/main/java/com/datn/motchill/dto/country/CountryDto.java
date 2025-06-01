package com.datn.motchill.dto.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.Country}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements Serializable {
    Long id;
    String name;
    String slug;
}