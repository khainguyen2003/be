package com.datn.motchill.shared.dto;

import com.datn.motchill.admin.common.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Lớp chứa kết quả trả về từ việc gọi stored procedure.
 * @param <T> Kiểu dữ liệu trả về
 */
@Data
@NoArgsConstructor
public class ResponseProcedure<T> {
    private String status;
    private String message;
    private List<T> data;

    public ResponseProcedure(String status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return Constants.SUCCESS_CODE.equalsIgnoreCase(status);
    }
}