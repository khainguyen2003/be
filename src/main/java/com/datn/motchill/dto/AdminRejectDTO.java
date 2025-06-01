package com.datn.motchill.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRejectDTO {
	
	@NotEmpty(message ="Danh sách ID không được để trống!")
	private String ids;
	
	@NotEmpty(message = "Lý do từ chối không được để trống!")
	private String lyDoTuChoi;
}
