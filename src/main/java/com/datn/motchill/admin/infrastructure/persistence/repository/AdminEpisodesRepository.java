package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminEpisodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminEpisodesRepository extends JpaRepository<AdminEpisodesEntity, Long>, JpaSpecificationExecutor<AdminEpisodesEntity> {
}