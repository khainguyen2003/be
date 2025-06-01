package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "director")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Director  extends BaseEntity {

    @Column(length = 150)
    private String name;

    @Column(length = 100)
    private String slug;
}