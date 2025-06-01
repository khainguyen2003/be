package com.datn.motchill.dto.episode;

import com.datn.motchill.dto.AdminPageFilterDTO;
import lombok.Data;

@Data
public class EpisodeFilterDTO extends AdminPageFilterDTO {
    private Long movieId;
    private String search;
}
