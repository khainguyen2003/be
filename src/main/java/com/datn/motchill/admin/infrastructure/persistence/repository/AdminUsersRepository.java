package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminUsersRepository extends JpaRepository<AdminUsersEntity, Long>, JpaSpecificationExecutor<AdminUsersEntity> {
}