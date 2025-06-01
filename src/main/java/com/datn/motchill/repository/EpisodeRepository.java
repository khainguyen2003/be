package com.datn.motchill.repository;

import com.datn.motchill.dto.episode.EpisodeSummaryDto;
import com.datn.motchill.entity.Episode;
import com.datn.motchill.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long>, JpaSpecificationExecutor<Episode> {
    Integer countByMovieId(Long movieId);

    @Query("""
        SELECT e.id AS id,
               e.name AS name,
               e.slug AS slug,
               e.episodeNumber AS episodeNumber,
               e.embed AS embed,
               e.m3u8 AS m3u8
        FROM Episode e
        WHERE e.movie.id = :movieId
        ORDER BY e.episodeNumber ASC
    """)
    List<EpisodeSummaryDto> findEpisodeSummaryByMovieId(Long movieId);

    List<Episode> findByMovieId(Long movieId);

    List<Episode> findAllByMovieIdIn(List<Long> movieId);

    Optional<Episode> findByMovieIdAndSlug(Long movieId, String slug);

    @Query("SELECT MAX(e.episodeNumber) FROM Episode e WHERE e.movie.id = :movieId")
    Integer findMaxEpisodeNumberByMovieId(Long movieId);
    
    boolean existsByMovieAndEpisodeNumber(Movie movie, Integer episodeNumber);
    
    void deleteByMovie(Movie movie);
    
    void deleteByMovieId(Long movieId);
}