package com.datn.motchill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datn.motchill.entity.Actor}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorDto implements Serializable {
    private Long id;
    private String name;
}