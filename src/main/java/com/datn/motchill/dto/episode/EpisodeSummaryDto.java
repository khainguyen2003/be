package com.datn.motchill.dto.episode;

public interface EpisodeSummaryDto {
    Long getId();
    String getName();
    String getSlug();
    Integer getEpisodeNumber();
    String getEmbed();
    String getM3u8();
}
