package com.datn.motchill.dto;


import com.datn.motchill.common.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminBaseResponse {
    private String status;
    private String message;
    private Object data;
    private List<Object> datas;
   
    public AdminBaseResponse success() {
        this.setMessage(Constants.SUCCESS_DESC);
        return this;
    }
    
    public AdminBaseResponse initData(Object data) {
        this.setData(data);
        return this;
    }

    public AdminBaseResponse success(Object data) {
        this.setMessage(Constants.SUCCESS_DESC);
        this.setData(data);
        return this;
    }

    public AdminBaseResponse fail() {
        this.setMessage(Constants.FAIL_DESC);
        return this;
    }

    public AdminBaseResponse dataInvalid() {
        this.setStatus(Constants.DATA_INVALID_CODE);
        this.setMessage(Constants.DATA_INVALID_CODE_DESC);
        return this;
    }
}
