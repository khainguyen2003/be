package com.datn.motchill.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "TBL_REVIEWS")
public class AdminReviewEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private AdminUsersEntity user;

    @ManyToOne
    private AdminMoviesEntity movie;

    private String content;

    private int rating; // 1 to 5

    private Date createdAt;
}
