package com.datn.motchill.admin.presentation.enums;

import com.datn.motchill.admin.common.converters.AbstractBaseEnumConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MovieTypeEnum implements AdminBaseEnum{
    SINGLE(1, "Phim lẻ"),
    SERIES(2, "Phim bộ");

    @JsonValue
    private final Integer key;

    private final String value;

    @Converter(autoApply = true)
    public static class MovieTypeEnumConverter extends AbstractBaseEnumConverter<MovieTypeEnum> {
        public MovieTypeEnumConverter() {
            super(MovieTypeEnum.class);
        }
    }

    public static MovieTypeEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (MovieTypeEnum isDisplay : MovieTypeEnum.values()) {
            if (isDisplay.getKey().equals(key)) {
                return isDisplay;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
