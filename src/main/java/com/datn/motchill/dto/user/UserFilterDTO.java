package com.datn.motchill.dto.user;

import com.datn.motchill.dto.AdminPageFilterDTO;
import com.datn.motchill.enums.UserRoleEnum;
import lombok.Data;

import java.util.List;

@Data
public class UserFilterDTO extends AdminPageFilterDTO {
    private String search;

    private List<UserRoleEnum> role;
}
