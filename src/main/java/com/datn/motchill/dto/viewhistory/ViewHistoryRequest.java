package com.datn.motchill.dto.viewhistory;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for creating or updating ViewHistory
 */
@Data
public class ViewHistoryRequest {
    
    @NotNull(message = "Movie ID không được để trống")
    private Long movieId;
    
    private Long episodeId;
    
    private Integer watchTime;
    
    private Integer lastPosition;
    
    private Boolean completed;
}
