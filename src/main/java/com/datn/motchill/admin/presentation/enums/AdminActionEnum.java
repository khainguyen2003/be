package com.datn.motchill.admin.presentation.enums;

import com.datn.motchill.admin.common.converters.AbstractBaseEnumConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminActionEnum implements AdminBaseEnum {
    SAVE_DRAFT(1, "Tạo mới"),
    UPDATE_DRAFT(2, "Sửa"),
    SAVE_APPROVE(3, "Lưu và phê duyệt"),
    UPDATE_APPROVE(4, "Sửa và phê duyệt"),
    UPDATE_SEND_APPROVE(5, "Sửa và gửi duyệt"),
    SAVE_SEND_APPROVE(6, "Lưu và gửi duyệt"),
    SEND_APPROVE(7, "Gửi Duyệt"),
    APPROVE(8, "Phê duyệt"),
    REJECT(9, "Từ chối"),
    CANCEL_APPROVAL(10, "Hủy phê duyệt"),
    DELETE(11, "Xóa bản ghi"),
    IMPORT_EXCEL(12, "Nhập excel"),
    UPDATE(13, "Cập nhật"),
	SEND_DETELE(14, "Gửi duyệt xóa"),
	SYS(15, "Đồng bộ");

    private Integer key;

    @JsonValue
    private String value;

    @Converter(autoApply = true)
    public static class ActionEnumConverter extends AbstractBaseEnumConverter<AdminActionEnum> {
        public ActionEnumConverter() {
            super(AdminActionEnum.class);
        }
    }

    public static AdminActionEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (AdminActionEnum param : AdminActionEnum.values()) {
            if (param.getKey().equals(key)) {
                return param;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }

}
