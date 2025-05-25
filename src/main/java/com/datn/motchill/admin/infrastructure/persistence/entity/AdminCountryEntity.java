package com.datn.motchill.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TBL_COUNTRY")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminCountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ISO_CODE", length = 2, nullable = false)
    private String isoCode;

    @Column(name = "THREE_ISO_CODE", length = 3)
    private String threeIsoCode;

    @Column(name = "COUNTRY_NAME", length = 50)
    private String countryName;

    @Column(name = "country_full_name", length = 255)
    private String countryFullName;
}
