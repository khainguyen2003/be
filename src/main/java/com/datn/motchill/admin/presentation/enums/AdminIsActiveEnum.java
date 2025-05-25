package com.datn.motchill.admin.presentation.enums;

import com.datn.motchill.admin.common.converters.AbstractBaseEnumConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminIsActiveEnum implements AdminBaseEnum {
    INACTIVE(0, "Không hoạt động"),
    ACTIVE(1, "Hoạt động");

    @JsonValue
    private final Integer key;

    private final String value;
    
    @Converter(autoApply = true)
    public static class IsActiveEnumConverter extends AbstractBaseEnumConverter<AdminIsActiveEnum> {
        public IsActiveEnumConverter() {
            super(AdminIsActiveEnum.class);
        }
    }
    
    public static AdminIsActiveEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (AdminIsActiveEnum isActive : AdminIsActiveEnum.values()) {
            if (isActive.getKey().equals(key)) {
                return isActive;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }

}
