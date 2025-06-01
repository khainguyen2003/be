package com.datn.motchill.repository;

import com.datn.motchill.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    
    Optional<Country> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Country t WHERE t.slug = :slug OR t.name = :name")
    boolean existsBySlugOrName(@Param("slug") String slug, @Param("name") String name);

    List<Country> findAllByOrderByNameAsc();
}