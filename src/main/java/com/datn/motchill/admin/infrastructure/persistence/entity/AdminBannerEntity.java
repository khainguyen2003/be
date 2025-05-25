package com.datn.motchill.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_BANNER")
public class AdminBannerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SLUG", length = 1000)
    private String slug;

    @Column(name = "NAME", columnDefinition = "TEXT")
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "LOGO_URL", length = 4000)
    private String logoUrl;

    @Column(name = "VIDEO_URL", length = 4000)
    private String videoUrl;
}
