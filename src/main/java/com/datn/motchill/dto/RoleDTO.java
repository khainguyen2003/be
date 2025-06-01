package com.datn.motchill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO extends AbstractDTO<RoleDTO>{
	
	@NotBlank(message = "{blankCodeRole}")
	@NotEmpty(message = "{emptyCodeRole}")
	private String code;

	@NotBlank(message = "{blankNameRole}")
	@NotEmpty(message = "{emptyNameRole}")
	private String name;
}