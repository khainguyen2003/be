package com.datn.motchill.dto;

import com.datn.motchill.entity.Episode;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link Episode}
 */
@Data
public class EpisodeDto implements Serializable {
    Long id;
    String title;
    int episodeNumber;
    String videoUrl;
}