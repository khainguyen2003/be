package com.datn.motchill.enums;

import com.datn.motchill.common.converters.AbstractBaseEnumConverter;
import com.datn.motchill.shared.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IsActiveEnum implements BaseEnum{
    INACTIVE(0, "Không hoạt động"),
    ACTIVE(1, "Hoạt động");

    @JsonValue
    private final Integer key;

    private final String value;
    
    @Converter(autoApply = true)
    public static class IsActiveEnumConverter extends AbstractBaseEnumConverter<IsActiveEnum> {
        public IsActiveEnumConverter() {
            super(IsActiveEnum.class);
        }
    }
    
    public static IsActiveEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (IsActiveEnum isActive : IsActiveEnum.values()) {
            if (isActive.getKey().equals(key)) {
                return isActive;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }

}
