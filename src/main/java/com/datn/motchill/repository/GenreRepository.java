package com.datn.motchill.repository;

import com.datn.motchill.admin.presentation.enums.StatusEnum;
import com.datn.motchill.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {
    @Query("SELECT COUNT(*) > 0 FROM Genre g WHERE (g.name = :name OR g.slug = :slug)")
    boolean isExist(String name, String slug);

    @Query("SELECT COUNT(*) > 0 FROM Genre g WHERE (g.name = :name OR g.slug = :slug) AND g.id <> :id")
    boolean isExistAndIdNot(String name, String slug, Long id);
}