package com.datn.motchill.admin.presentation.dto.genre;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminGenreEntity;
import com.datn.motchill.admin.presentation.dto.AdminBaseResponse;
import com.datn.motchill.admin.presentation.dto.AdminResponseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link AdminGenreEntity}
 */
@Data
public class AdminGenreResponseDTO extends AdminResponseDTO implements Serializable {
    private Long id;

    private String name;

    private String title;

    private String slug;

    private Integer order;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AdminGenreResponseDTO that = (AdminGenreResponseDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}