package com.datn.motchill.admin.presentation.dto.quanLyHoatDong;

import com.datn.motchill.admin.presentation.enums.AdminActionEnum;
import com.datn.motchill.admin.presentation.enums.AdminParamEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AdminQuanLyHoatDongResponseDTO {
    private Long id;

    private String nguoiDung;

    private String IP;

    private AdminParamEnum chucNang;

    private AdminActionEnum thaoTac;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy HH:mm:ss", timezone = "GMT+7")
    private Timestamp ngayThucHien;

    private String noiDung;

    private Long idBanGhi;

    private String lyDoTuChoi;

}
