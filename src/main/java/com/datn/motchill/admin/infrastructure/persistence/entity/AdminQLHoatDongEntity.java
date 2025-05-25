package com.datn.motchill.admin.infrastructure.persistence.entity;

import com.datn.motchill.admin.presentation.enums.AdminActionEnum;
import com.datn.motchill.admin.presentation.enums.AdminParamEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
     * Author: vietcd_llq
     * Since:
     * Description:
*/
@Entity
@Table(name = "AUDIT_LOG")
@Getter
@Setter
public class AdminQLHoatDongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 50)
    private String nguoiDung;

    @Column(name = "IP", length = 20)
    private String IP;


    @Column(name = "FUNCTION")
    @Convert(converter = AdminParamEnum.ParamEnumConverter.class)
    private AdminParamEnum chucNang;

    @Column(name = "ACTION")
    @Convert(converter = AdminActionEnum.ActionEnumConverter.class)
    private AdminActionEnum thaoTac;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy HH:mm:ss", timezone = "GMT+7")
    @Column(name = "ACTION_DATE")
    private Timestamp ngayThucHien;

    @Column(name = "CONTENT")
    private String noiDung;

    @Column(name = "DATA_ID")
    private Long idBanGhi;

    @Column(name = "REASON_REJECT")
    private String lyDoTuChoi;

}
