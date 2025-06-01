package com.datn.motchill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponseDTO {
    private String sourceUrl;
    private String m3u8;
    private String hlsFolder;
}
