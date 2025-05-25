package com.datn.motchill.admin.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOptionDTO {
	
	private String value;
	private String label;
	
	public AdminOptionDTO(Object value, Object label) {
		this.value = value.toString();
		this.label = label.toString();
	}
	
}
