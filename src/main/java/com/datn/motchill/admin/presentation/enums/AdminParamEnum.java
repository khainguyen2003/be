package com.datn.motchill.admin.presentation.enums;

import com.datn.motchill.admin.common.converters.AbstractBaseEnumConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@Converter(autoApply = true)
@AllArgsConstructor
public enum AdminParamEnum implements AdminBaseEnum {
    GENRE(1, "Thể loại phim");

    private Integer key;
    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Converter(autoApply = true)
    public static class ParamEnumConverter extends AbstractBaseEnumConverter<AdminParamEnum> {
        public ParamEnumConverter() {
            super(AdminParamEnum.class);
        }
    }

    public static AdminParamEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (AdminParamEnum param : AdminParamEnum.values()) {
            if (param.getKey().equals(key)) {
                return param;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
