package com.datn.motchill.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TBL_USERS")
public class AdminUsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "FULL_NAME", length = 150)
    private String fullName;

    @Column(name = "EMAIL", length = 200)
    private String email;

    @Column(name = "ROLE_CODE", length = 50)
    private String roleCode;

    @Column(name = "PASSWORD", length = 255)
    private String password;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
}

