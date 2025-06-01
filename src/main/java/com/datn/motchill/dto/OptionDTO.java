package com.datn.motchill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO {
	
	private String value;
	private String label;
	
	public OptionDTO(Object value, Object label) {
		this.value = value.toString();
		this.label = label.toString();
	}
	
}
