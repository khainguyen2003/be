package com.datn.motchill.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TBL_GENRE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminGenreEntity extends AdminBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 255)
    @Comment("tên thể loại ngắn gọn (ví dụ: \"Action\", \"Science Fiction\")")
    private String name;

    @Column(name = "TITLE", length = 2000)
    private String title;

    @Column(name = "SLUG", length = 255)
    private String slug;

    @Column(name = "PARENT1")
    private Long parent1;

    @Column(name = "PARENT2")
    private Long parent2;

    @Column(name = "DISPLAY_ORDER", scale = 3, precision = 0)
    private Integer displayOrder;
}
