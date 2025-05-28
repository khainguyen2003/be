package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "countries")
@Data
public class Country  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String slug;

//    @OneToMany(mappedBy = "country")
//    private List<Movie> movies;
}
