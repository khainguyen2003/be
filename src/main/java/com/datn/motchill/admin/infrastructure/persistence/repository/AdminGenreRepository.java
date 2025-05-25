package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminGenreEntity;
import com.datn.motchill.admin.presentation.enums.AdminStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface AdminGenreRepository extends JpaRepository<AdminGenreEntity, Long>, JpaSpecificationExecutor<AdminGenreEntity> {
    @Query("SELECT COUNT(*) > 0 FROM AdminGenreEntity g WHERE (g.name = :name OR g.title = :title)")
    boolean isExist(String name, String title);

    @Query("SELECT COUNT(*) > 0 FROM AdminGenreEntity g WHERE (g.name = :name OR g.title = :title) AND g.id <> :id")
    boolean isExistAndIdNot(String name, String title, Long id);

    @Modifying
    @Query("update AdminGenreEntity m set m.newData = :newData where m.id = :id")
    void updateNewData(String newData, Long id);

    @Modifying
    @Query("update AdminGenreEntity m set m.status = :status where m.id IN :ids")
    void updateStatusByIds(AdminStatusEnum status, Set<Long> ids);

    @Modifying
    @Query("update AdminGenreEntity m set m.newData = :newData, m.status =:status  where m.id = :id")
    void updateNewDataAndSetStatusById(String newData, Long id, AdminStatusEnum status);
}