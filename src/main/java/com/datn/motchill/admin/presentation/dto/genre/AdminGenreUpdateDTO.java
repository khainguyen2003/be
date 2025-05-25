package com.datn.motchill.admin.presentation.dto.genre;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminGenreUpdateDTO {
    private Long id;
    @Size(max = 50, message = "Tên thể loại ngắn gọn không được vượt quá 50 ký tự")
    @NotBlank(message = "Tên thể loại ngắn gọn không được bỏ trống")
    private String name;

    @Size(max = 2000, message = "Tên hiển thị của thể loại không được vượt quá 2000 ký tự")
    @NotBlank(message = "Tên hiển thị của thể loại không được bỏ trống")
    private String title;

    @Size(max = 100, message = "Đường dẫn của thể loại không được vượt quá 100 ký tự")
    private String slug;

    @Min(value = 0, message = "Thứ tự sắp xếp của thể loại không được nhỏ hơn 0")
    private Integer displayOrder;
}
