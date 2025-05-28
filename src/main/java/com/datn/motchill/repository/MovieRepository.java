package com.datn.motchill.repository;

import com.datn.motchill.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.episodes WHERE m.slug = :slug")
    List<Movie> findAllByIdInWithEpisodes(@Param("slug") String slug);
}