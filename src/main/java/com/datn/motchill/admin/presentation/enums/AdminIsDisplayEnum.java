package com.datn.motchill.admin.presentation.enums;

import com.datn.motchill.admin.common.converters.AbstractBaseEnumConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminIsDisplayEnum implements AdminBaseEnum {
    NOT_APPROVE(1, "Cho phép xóa"),
    APPROVED(2, "Không cho phép xóa");

    @JsonValue
    private final Integer key;

    private final String value;
    
    @Converter(autoApply = true)
    public static class IsDisplayEnumConverter extends AbstractBaseEnumConverter<AdminIsDisplayEnum> {
        public IsDisplayEnumConverter() {
            super(AdminIsDisplayEnum.class);
        }
    }
    
    public static AdminIsDisplayEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (AdminIsDisplayEnum isDisplay : AdminIsDisplayEnum.values()) {
            if (isDisplay.getKey().equals(key)) {
                return isDisplay;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
