package com.datn.motchill.admin.common.utils;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminBaseEntity;
import com.datn.motchill.admin.presentation.enums.AdminIsDisplayEnum;
import com.datn.motchill.admin.presentation.enums.AdminStatusEnum;

import java.util.List;

public class ValidWorkFlow {

    // Kiểm tra có được phép cập nhật hay không
    // Điều kiện: Trạng thái là (lưu nháp hoặc hủy phê duyệt) và trạng thái hiển thị là hiển thị
    public static <T extends AdminBaseEntity> boolean isUpdateValid(T entity) {
        AdminStatusEnum status = entity.getStatus();
        return status == AdminStatusEnum.SAVE_DRAF || status == AdminStatusEnum.CANCEL_APPROVAL || status == AdminStatusEnum.REJECTED;
    }

    // Kiểm tra có được phép hủy phê duyệt hay không
    // Điều kiện: Trạng thái là Đã phê duyệt và trạng thái hiển thị là hiển thị
    public static <T extends AdminBaseEntity> boolean isCancelApproveValid(T entity) {
        AdminStatusEnum status = entity.getStatus();
        return status == AdminStatusEnum.APPROVED;
    }

    // Kiểm tra có được phép xóa hay không
    // Điều kiện: Trạng thái (SAVE_DRAF hoặc REJECTED) và DISPLAY = NOT_APPROVE 
    public static <T extends AdminBaseEntity> boolean isDeleteValid(T entity) {
        AdminStatusEnum status = entity.getStatus();
        AdminIsDisplayEnum displayEnum = entity.getIsDisplay();
        return (status == AdminStatusEnum.SAVE_DRAF || status == AdminStatusEnum.REJECTED) && (displayEnum == AdminIsDisplayEnum.NOT_APPROVE);
    }

    // Kiểm tra danh sách có được phép cập nhật hay không
    public static <T extends AdminBaseEntity> boolean isUpdateListValid(List<T> list) {
        return list.stream().allMatch(ValidWorkFlow::isUpdateValid);
    }

    // Kiểm tra danh sách có được phép hủy phê duyệt hay không
    public static <T extends AdminBaseEntity> boolean isCannelApproveListValid(List<T> list) {
        return list.stream().allMatch(ValidWorkFlow::isCancelApproveValid);
    }

    // Kiểm tra danh sách có được phép xóa hay không
    public static <T extends AdminBaseEntity> boolean isDeleteListValid(List<T> list) {
        return list.stream().allMatch(ValidWorkFlow::isDeleteValid);
    }

    // Kiểm tra có được phép duyệt hay không
    // Điều kiện: Trạng thái là (mới hoặc hủy phê duyệt) và trạng thái hiển thị là hiển thị
    public static <T extends AdminBaseEntity> boolean isApproveValid(T entity) {
        AdminStatusEnum status = entity.getStatus();
        return status == AdminStatusEnum.WAIT_APPROVE;
    }

    // Kiểm tra có được phép từ chối duyệt hay không
    // Điều kiện: Trạng thái là chờ duyệt và trạng thái hiển thị là hiển thị
    public static <T extends AdminBaseEntity> boolean isRejectValid(T entity) {
        AdminStatusEnum status = entity.getStatus();
        return status == AdminStatusEnum.WAIT_APPROVE;
    }

    // Kiểm tra có được phép gửi duyệt hay không?
    // Điều kiện: Trạng thái là (lưu nháp hoặc cancel_approval) và hirnt thị
    public static <T extends AdminBaseEntity> boolean isSendApproveValid(T entity) {
        AdminStatusEnum status = entity.getStatus();
        return status == AdminStatusEnum.SAVE_DRAF || status == AdminStatusEnum.CANCEL_APPROVAL || status == AdminStatusEnum.REJECTED;
    }

    // Kiểm tra danh sách có được phép hủy phê duyệt hay không
    public static <T extends AdminBaseEntity> boolean isApproveListValid(List<T> list) {
        return list.stream().allMatch(ValidWorkFlow::isApproveValid);
    }

    // Kiểm tra danh sách có được phép từ chối hay không
    public static <T extends AdminBaseEntity> boolean isRejectListValid(List<T> list) {
        return list.stream().allMatch(ValidWorkFlow::isRejectValid);
    }

    // Kiểm tra danh sách có được phép gửi duyệt hay không
    public static <T extends AdminBaseEntity> boolean isSendApproveListValid(List<T> list) {
        return list.stream().allMatch(ValidWorkFlow::isSendApproveValid);
    }
}


