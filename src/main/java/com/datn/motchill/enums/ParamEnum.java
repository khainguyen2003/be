package com.datn.motchill.enums;

import com.datn.motchill.common.converters.AbstractBaseEnumConverter;
import com.datn.motchill.shared.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@Converter(autoApply = true)
@AllArgsConstructor
public enum ParamEnum implements BaseEnum {
    GENRE(1, "Thể loại phim");

    private Integer key;
    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Converter(autoApply = true)
    public static class ParamEnumConverter extends AbstractBaseEnumConverter<ParamEnum> {
        public ParamEnumConverter() {
            super(ParamEnum.class);
        }
    }

    public static ParamEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (ParamEnum param : ParamEnum.values()) {
            if (param.getKey().equals(key)) {
                return param;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
