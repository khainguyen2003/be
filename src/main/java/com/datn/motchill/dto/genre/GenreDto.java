package com.datn.motchill.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.Genre}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto implements Serializable {
    Long id;
    String name;
    String slug;
}
