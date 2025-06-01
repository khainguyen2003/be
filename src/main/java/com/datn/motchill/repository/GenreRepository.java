package com.datn.motchill.repository;

import com.datn.motchill.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {
    
    Optional<Genre> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}