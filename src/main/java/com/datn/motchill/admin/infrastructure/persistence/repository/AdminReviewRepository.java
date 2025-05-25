package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminReviewRepository extends JpaRepository<AdminReviewEntity, Long>, JpaSpecificationExecutor<AdminReviewEntity> {
}