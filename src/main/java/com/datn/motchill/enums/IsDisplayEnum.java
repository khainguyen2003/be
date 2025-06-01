package com.datn.motchill.enums;

import com.datn.motchill.common.converters.AbstractBaseEnumConverter;
import com.datn.motchill.shared.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IsDisplayEnum implements BaseEnum {
    NOT_APPROVE(1, "Cho phép xóa"),
    APPROVED(2, "Không cho phép xóa");

    @JsonValue
    private final Integer key;

    private final String value;
    
    @Converter(autoApply = true)
    public static class IsDisplayEnumConverter extends AbstractBaseEnumConverter<IsDisplayEnum> {
        public IsDisplayEnumConverter() {
            super(IsDisplayEnum.class);
        }
    }
    
    public static IsDisplayEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (IsDisplayEnum isDisplay : IsDisplayEnum.values()) {
            if (isDisplay.getKey().equals(key)) {
                return isDisplay;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
