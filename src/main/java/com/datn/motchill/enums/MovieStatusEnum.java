package com.datn.motchill.enums;

import com.datn.motchill.common.converters.AbstractBaseEnumConverter;
import com.datn.motchill.shared.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MovieStatusEnum implements BaseEnum {
    SAVE_DRAFT(1, "Tạo mới"),
    WAIT_APPROVE(2, "Chờ duyệt"),
    APPROVED(3, "Đã phê duyệt"),
    CANCEL_APPROVED(4, "Hủy duyệt"),
    REJECTED(5, "Từ chối"),
    PUBLISHED(6, "Đã phát hành"),
    UNPUBLISHED(7, "Hủy phát hành"),
    ARCHIVED(8, "Đã hết hạn");

    @JsonValue
    private final Integer key;
    private final String value;

    @Converter(autoApply = true)
    public static class MovieStatusEnumConverter extends AbstractBaseEnumConverter<MovieStatusEnum> {
        public MovieStatusEnumConverter() {
            super(MovieStatusEnum.class);
        }
    }

    public static MovieStatusEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (MovieStatusEnum status : MovieStatusEnum.values()) {
            if (status.getKey().equals(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
