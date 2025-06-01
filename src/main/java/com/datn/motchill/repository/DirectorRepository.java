package com.datn.motchill.repository;

import com.datn.motchill.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    
    Optional<Director> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    boolean existsByName(String name);
}