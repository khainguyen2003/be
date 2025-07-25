package com.datn.motchill.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createdBy")
    @CreatedBy
    private String createdBy;

    @Column(name = "modifiedBy")
    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "createdDate")
    @CreatedDate
    private Date createdDate;

    @Column(name = "modifedDate")
    @LastModifiedDate
    private Date modifiedDate;

}
