package com.datn.motchill.repository;

import com.datn.motchill.entity.Movie;
import com.datn.motchill.entity.User;
import com.datn.motchill.entity.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
    Optional<ViewHistory> findByUserAndMovie(User user, Movie movie);
    Optional<ViewHistory> findByUserAndMovieAndEpisodeIsNull(User user, Movie movie);
    Optional<ViewHistory> findByUserAndMovieAndEpisodeId(User user, Movie movie, Long episodeId);
}
