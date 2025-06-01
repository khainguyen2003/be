package com.datn.motchill.enums;


import com.datn.motchill.common.converters.AbstractBaseEnumConverter;
import com.datn.motchill.shared.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum StatusEnum implements BaseEnum {

    SAVE_DRAF(1, "Tạo mới"),
    WAIT_APPROVE(3, "Chờ phê duyệt"),
    APPROVED(4, "Đã phê duyệt"),
    REJECTED(5,"Từ chối"),
    CANCEL_APPROVAL(7, "Hủy phê duyệt");

    @JsonValue
    private final Integer key;
    private final String value;

    @Converter(autoApply = true)
    public static class StatusEnumConverter extends AbstractBaseEnumConverter<StatusEnum> {
        public StatusEnumConverter() {
            super(StatusEnum.class);
        }
    }
    
    public static StatusEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getKey().equals(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
