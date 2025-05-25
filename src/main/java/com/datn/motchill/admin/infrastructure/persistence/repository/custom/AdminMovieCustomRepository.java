package com.datn.motchill.admin.infrastructure.persistence.repository.custom;

import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesFilterDto;
import com.datn.motchill.admin.presentation.dto.movies.AdminMovieResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminMovieCustomRepository {
    Page<AdminMovieResponseDTO> findAll(AdminMoviesFilterDto filter, Pageable pageable);

    List<AdminMovieResponseDTO> findAll(AdminMoviesFilterDto filter);
}
