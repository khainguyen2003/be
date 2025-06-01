package com.datn.motchill.dto.director;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.Director}
 */
@Value
public class DirectorDto implements Serializable {
    Long id;
    String name;
}