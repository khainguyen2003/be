package com.datn.motchill.repository;

import com.datn.motchill.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    Integer countByMovieId(Long movieId);

    List<Episode> findByMovieId(Long movieId);
}