package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "actors")
@Data
public class Actor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String name;
}
