package com.datn.motchill.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.Country}
 */
@Value
public class CountryDto implements Serializable {
    Long id;
    String name;
    String slug;
}