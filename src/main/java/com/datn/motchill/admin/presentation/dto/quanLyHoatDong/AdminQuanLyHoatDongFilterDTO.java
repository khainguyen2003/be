package com.datn.motchill.admin.presentation.dto.quanLyHoatDong;

import com.datn.motchill.admin.presentation.dto.AdminPageFilterDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminQuanLyHoatDongFilterDTO extends AdminPageFilterDTO {
	private Integer chucNang;
	private Integer idBanGhi;
}
