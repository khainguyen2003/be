package com.datn.motchill.admin.presentation.enums;

import com.datn.motchill.admin.common.converters.AbstractBaseEnumConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdminMovieStatusEnum implements AdminBaseEnum  {
    COLLECTED(1, "Tạo mới"),
    PENDING_APPROVE_COLLECTED(2, "Chờ kiểm duyệt lần 1"),
    REJECTED_COLLECTING(3, "Từ chối kiểm duyệt lần 1"),
    CANCEL_APPROVE_COLLECTING(4, "Hủy duyệt lần 1"),

    PENDING_TRANSLATE(5, "Chờ dịch"),
    PENDING_APPROVE_TRANSLATE(6, "Chờ duyệt dịch"),
    TRANSLATED(7, "Đã dịch"),
    REJECT_TRANSLATE(8, "Từ chối bản dịch"),
    CANCEL_APPROVE_TRANSLATE(9, "Hủy duyệt dịch"),

    PENDING_EDIT(10, "Chờ biên tập"),
    EDITED(11, "Đã biên tập"),
    REJECT_EDIT(12, "Bản biên tập bị từ chối"),
    CANCEL_APPROVE_EDIT(13, "Hủy duyệt biên tập"),

    APPROVED(14, "Đã duyệt"),
    PUBLISHED(15, "Đã phát hành"),
    UNPUBLISHED(16, "Hủy phát hành"),
    ARCHIVED(17, "Đã hết hạn");

    @JsonValue
    private final Integer key;
    private final String value;

    @Converter(autoApply = true)
    public static class MovieStatusEnumConverter extends AbstractBaseEnumConverter<AdminMovieStatusEnum> {
        public MovieStatusEnumConverter() {
            super(AdminMovieStatusEnum.class);
        }
    }

    public static AdminMovieStatusEnum fromKey(Integer key) {
        if (key == null) {
            return null; // Hoặc có thể ném ra ngoại lệ tùy theo yêu cầu của hệ thống
        }
        for (AdminMovieStatusEnum status : AdminMovieStatusEnum.values()) {
            if (status.getKey().equals(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
