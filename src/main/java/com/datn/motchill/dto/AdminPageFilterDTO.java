package com.datn.motchill.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPageFilterDTO {
    private Integer page;
    private Integer limit;
    private String sort;
}