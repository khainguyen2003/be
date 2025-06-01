package com.datn.motchill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.datn.motchill.entity.Country;
import com.datn.motchill.entity.Genre;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.entity.Tag;
import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.enums.MovieTypeEnum;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    
    Optional<Movie> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    
    Page<Movie> findByGenresContaining(Genre genre, Pageable pageable);
//
//    Page<Movie> findByCountriesContaining(Country country, Pageable pageable);
//
//    Page<Movie> findByTagsContaining(Tag tag, Pageable pageable);

    Page<Movie> findByMovieType(MovieTypeEnum type, Pageable pageable);
    
    Page<Movie> findByStatus(MovieStatusEnum status, Pageable pageable);

    boolean existsByGenresContaining(Genre genre);
}