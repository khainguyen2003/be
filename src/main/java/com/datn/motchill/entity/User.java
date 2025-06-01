package com.datn.motchill.entity;

import com.datn.motchill.enums.UserRoleEnum;
import com.datn.motchill.enums.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Column
    @NotBlank
    @Size(min = 3, max = 100)
    private String password;

    @Column(name = "fullname")
    private String fullName;

    @Column
    @NotBlank
    @Size(min = 6, max = 100)
    @Email
    private String email;

    @Column
    private String phone;

    @Column
    @Lob
    private String avatar;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.USER;

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status = UserStatusEnum.ACTIVE;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ViewHistory> viewHistory;
}
