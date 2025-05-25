package com.datn.motchill.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TBL_ACTOR")
@Getter
@Setter
public class AdminActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTOR_ID")
    private Integer id;

    @Column(name = "ACTOR_NAME", length = 120)
    private String name;

    @Column(name = "ACTOR_NAME1", length = 120)
    private String name1;

    @Column(name = "ACTOR_NAME_KD", length = 120)
    private String nameKD;

    @Column(name = "ACTOR_BIRTHDAY", length = 10)
    private String birthday;

    @Column(name = "ACTOR_LOCATION", length = 255)
    private String location;

    @Column(name = "ACTOR_HEIGHT", length = 50)
    private String height;

    @Column(name = "ACTOR_IMG", length = 255)
    private String image;

    @Column(name = "ACTOR_MOVIE", columnDefinition = "text")
    private String movie;

    @Column(name = "ACTOR_INFO", columnDefinition = "text")
    private String info;

    @Column(name = "ACTOR_RANK")
    private Integer rank;

    @Column(name = "ACTOR_PROFILE", length = 255)
    private String profileUrl;
}
