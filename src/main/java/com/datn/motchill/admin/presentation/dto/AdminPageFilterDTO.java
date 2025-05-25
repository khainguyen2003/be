package com.datn.motchill.admin.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPageFilterDTO {
    private Integer page;
    private Integer limit;
    private String sort;
}