package com.datn.motchill.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.Tag}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto implements Serializable {
    Long id;
    String name;
    String slug;
}
