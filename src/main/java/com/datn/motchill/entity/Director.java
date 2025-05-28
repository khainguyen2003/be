package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "director")
@Data
public class Director  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String name;

    // getters, setters...
}