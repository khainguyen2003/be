package com.datn.motchill.customer.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageFilterDTO {
    private Integer page;
    private Integer limit;
    private String sort;
}