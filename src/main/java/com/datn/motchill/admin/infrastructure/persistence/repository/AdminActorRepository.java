package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminActorRepository extends JpaRepository<AdminActorEntity, Integer>, JpaSpecificationExecutor<AdminActorEntity> {
}