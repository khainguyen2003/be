package com.datn.motchill.dto.genre;

import com.datn.motchill.dto.AdminPageFilterDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GenreFilter extends AdminPageFilterDTO {
    private String search;
}
