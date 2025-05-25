package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminQLHoatDongEntity;
import com.datn.motchill.admin.presentation.enums.AdminParamEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuanLyHoatDongRepository extends JpaRepository<AdminQLHoatDongEntity,Long>, JpaSpecificationExecutor<AdminQLHoatDongEntity> {

   // Page<QLHoatDongEntity> findAllByIdBanGhiAndChucNangId(Long idBanGhi, ChucNangEnum chucNangEnum);

    Page<AdminQLHoatDongEntity> findAllByIdBanGhiAndChucNang(Integer idBanGhi, AdminParamEnum chucNang , Pageable pageable);

    List<AdminQLHoatDongEntity> findAllByIdBanGhiAndChucNang(Long idBanGhi, AdminParamEnum chucNang);


    @Modifying
    @Query("UPDATE AdminQLHoatDongEntity q SET q.idBanGhi = :idBanGhi WHERE q IN :list" )
    void updateListByIdBanGhi(Long idBanGhi, List<AdminQLHoatDongEntity> list);
}
