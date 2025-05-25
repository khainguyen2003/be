package com.datn.motchill.admin.infrastructure.persistence.entity;

import com.datn.motchill.admin.presentation.enums.AdminIsActiveEnum;
import com.datn.motchill.admin.presentation.enums.AdminIsDisplayEnum;
import com.datn.motchill.admin.presentation.enums.AdminStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class AdminBaseEntity {

    @Column(name = "STATUS")
    @Convert(converter = AdminStatusEnum.StatusEnumConverter.class)
    private AdminStatusEnum status;

    @Column(name = "IS_DISPLAY")
    @Convert(converter = AdminIsDisplayEnum.IsDisplayEnumConverter.class)
    private AdminIsDisplayEnum isDisplay;

    @Convert(converter = AdminIsActiveEnum.IsActiveEnumConverter.class)
    @Column(name = "IS_ACTIVE")
    private AdminIsActiveEnum isActive;

    @Column(name = "NEW_DATA", length = 4000)
    private String newData;

    // Hàm cài đặt trạng thái khi lưu nháp
    // Măc định trạng thái mới, không hoạt động, hiển thị
    public void setStatusSaveDraft() {
        this.setStatus(AdminStatusEnum.SAVE_DRAF);
        this.setIsActive(AdminIsActiveEnum.INACTIVE);
        this.setIsDisplay(AdminIsDisplayEnum.NOT_APPROVE);
    }

    // Hàm cài đặt trạng thái khi lưu và phê duyệt
    // Mặc định trạng thái đã phê duyệt, hoạt động, hiển thị
    public void setStatusSaveAndApprove() {
        this.setStatus(AdminStatusEnum.APPROVED);
        this.setIsDisplay(AdminIsDisplayEnum.APPROVED);
        this.setIsActive(AdminIsActiveEnum.ACTIVE);

        this.newData = null;

    }

    // Hàm cài đặt trạng thái khi phê duyệt
    // Mặc định trạng thái đã phê duyệt, hoạt động, hiển thị
    public void setStatusApprove() {
        this.setStatus(AdminStatusEnum.APPROVED);
        this.setIsDisplay(AdminIsDisplayEnum.APPROVED);
        this.setIsActive(AdminIsActiveEnum.ACTIVE);

        this.newData = null;

    }

    // Hàm cài đặt trạng thái khi hủy phê duyệt
    // Mặc định trạng thái hủy phê duyệt, không hoạt động, hiển thị
    public void setStatusCancelApproval() {
        this.setStatus(AdminStatusEnum.CANCEL_APPROVAL);
    }

    // Hàm cài đặt trạng thái lưu và đẩy duyệt
    // Mặc định trạng thái wait_approve, không hoạt động, hiển thị
    public void setStatusSaveAndSendApprove() {
        this.setStatus(AdminStatusEnum.WAIT_APPROVE);
        this.setIsActive(AdminIsActiveEnum.INACTIVE);
        this.setIsDisplay(AdminIsDisplayEnum.NOT_APPROVE);
    }

    public boolean isApproved() {
        return this.status == AdminStatusEnum.APPROVED;
    }

    public boolean isCancelApproved() {
        return this.status == AdminStatusEnum.CANCEL_APPROVAL;
    }

    public boolean isWaitApproved() {
        return this.status == AdminStatusEnum.WAIT_APPROVE;
    }

    public boolean isSaveDraft() {
        return this.status == AdminStatusEnum.SAVE_DRAF;
    }

    public boolean isReject() {
        return this.status == AdminStatusEnum.REJECTED;
    }
}