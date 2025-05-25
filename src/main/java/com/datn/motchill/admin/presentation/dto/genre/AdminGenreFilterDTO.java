package com.datn.motchill.admin.presentation.dto.genre;

import com.datn.motchill.admin.presentation.dto.AdminPageFilterDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminGenreFilterDTO extends AdminPageFilterDTO {
    @Size(max = 500, message = "Chọn quá nhiều phần tử")
    private String ids; // các ký tự id cách nhau bằng dấu ,

    @Size(max = 255, message = "Chuỗi tìm kiếm không được vượt quá 100 ký tự")
    private String search;

    private String status;

    private String isActive;

    private String isDisplay;
}
