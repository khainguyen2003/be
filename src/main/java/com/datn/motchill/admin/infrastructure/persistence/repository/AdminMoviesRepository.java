package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminMoviesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AdminMoviesRepository extends JpaRepository<AdminMoviesEntity, Long>, JpaSpecificationExecutor<AdminMoviesEntity> {

    @Query("SELECT COUNT(*) > 0 FROM AdminMoviesEntity m WHERE m.name = :name")
    boolean isExists(String name);

    @Query("SELECT COUNT(*) > 0 FROM AdminMoviesEntity m WHERE m.name = :name AND m.id <> :id")
    boolean isExistsAndIdNot(String name, Long id);

}