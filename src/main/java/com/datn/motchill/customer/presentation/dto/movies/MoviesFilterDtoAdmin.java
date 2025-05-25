package com.datn.motchill.customer.presentation.dto.movies;

import com.datn.motchill.admin.presentation.dto.AdminPageFilterDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * Author: khainv_llq<br/>
 * Since: 04/26/2025 07:44 AM<br/>
 * Description: 
 */
@Data
public class MoviesFilterDtoAdmin extends AdminPageFilterDTO implements Serializable {
    String genre;
    String search;
    Integer episode_number;
}