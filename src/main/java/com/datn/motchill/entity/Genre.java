package com.datn.motchill.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "GENRES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Genre extends BaseEntity {

    private String name;

    @Column(unique = true)
    private String slug;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private List<Movie> movies;
}
