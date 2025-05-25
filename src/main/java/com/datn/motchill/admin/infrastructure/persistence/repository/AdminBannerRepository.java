package com.datn.motchill.admin.infrastructure.persistence.repository;

import com.datn.motchill.admin.infrastructure.persistence.entity.AdminBannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBannerRepository extends JpaRepository<AdminBannerEntity, Long> {
}