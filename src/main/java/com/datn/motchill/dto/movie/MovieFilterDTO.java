package com.datn.motchill.dto.movie;

import com.datn.motchill.dto.AdminPageFilterDTO;
import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.enums.MovieTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * DTO for movie filtering
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieFilterDTO extends AdminPageFilterDTO {
    
    private String search;
    
    private Integer releaseYear;

    private List<Integer> types;
    private List<Integer> statuses;
    
    private Set<Long> genreIds;
    
    private Set<Long> countryIds;
    
    private Set<Long> directorIds;
    
    private Set<Long> tagIds;
    
    private String sortBy; // title, releaseYear, viewCount, rating, createdAt
    
    private String sortDirection; // asc, desc
}
