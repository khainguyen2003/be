package com.datn.motchill.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.Director}
 */
@Value
@AllArgsConstructor
public class DirectorDto implements Serializable {
    Long id;
    String name;
    String slug;
}
