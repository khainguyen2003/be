package com.datn.motchill.dto.episode;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EpisodeRequest {
    String name;
    String slug;
    String embed;
    String m3u8;

    private MultipartFile file;
}
