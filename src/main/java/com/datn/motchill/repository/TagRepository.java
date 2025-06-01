package com.datn.motchill.repository;

import com.datn.motchill.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
    @Query("SELECT COUNT(t) > 0 FROM Tag t WHERE t.slug = :slug OR t.name = :name")
    boolean exists(String slug, String name);
    List<Tag> findAllByOrderByNameAsc();
}
