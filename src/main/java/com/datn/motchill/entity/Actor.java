package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "actors")
@Data
public class Actor extends BaseEntity {

    @Column(length = 150)
    private String name;
}
