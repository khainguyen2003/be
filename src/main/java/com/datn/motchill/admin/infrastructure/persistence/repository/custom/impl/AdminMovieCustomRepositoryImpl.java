package com.datn.motchill.admin.infrastructure.persistence.repository.custom.impl;

import com.datn.motchill.admin.infrastructure.persistence.repository.custom.AdminMovieCustomRepository;
import com.datn.motchill.admin.presentation.dto.movies.AdminMoviesFilterDto;
import com.datn.motchill.admin.presentation.dto.movies.AdminMovieResponseDTO;
import com.datn.motchill.shared.services.ExecutorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AdminMovieCustomRepositoryImpl implements AdminMovieCustomRepository {
    @PersistenceContext
    EntityManager entityManager;

    private final ExecutorService executor;

    @Override
    public Page<AdminMovieResponseDTO> findAll(AdminMoviesFilterDto filter, Pageable pageable) {
        StringBuilder selectSql = new StringBuilder("""
            SELECT 
                m.ID,
                m.CASTS,
                m.CREATED_AT,
                m.DESCRIPTION,
                m.DIRECTOR,
                m.NAME,
                m.ORIGINAL_NAME,
                m.POSTER_URL,
                m.QUALITY,
                m.RATING_AVG,
                m.RELEASE_DATE,
                m.SLUG,
                m.SOURCES,
                m.THUMB_URL,
                m.TIME,
                m.TRAILER_URL,
                m.VIEWS
            FROM tbl_movies m        
        """);

        StringBuilder whereClause = new StringBuilder("WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Tìm theo tên, original_name
        if(StringUtils.hasText(filter.getName())) {
            whereClause.append(" AND m.NAME LIKE ? OR m.ORIGINAL_NAME LIKE ?");
            params.add("%"+filter.getName()+"%");
            params.add("%"+filter.getName()+"%");
        }

        if (filter.getGenre() != null && !filter.getGenre().isEmpty()) {
            Set<String> set = Arrays.stream(filter.getGenre().split(","))
                    .map(String::trim).filter(s->!s.isEmpty())
                    .collect(Collectors.toSet());
            if (!set.isEmpty()) {
                String inClause = set.stream().map(s->"?").collect(Collectors.joining(","));
                whereClause.append(" AND t.CURRENCY_CODE IN (").append(inClause).append(")");
                params.addAll(set);
            }
        }

        selectSql.append(whereClause);
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            selectSql.append(" ORDER BY ");

            List<String> orderClauses = new ArrayList<>();
            for (Sort.Order order : sort) {
                String property = order.getProperty();
                String direction = order.isAscending() ? "ASC" : "DESC";
                orderClauses.add(property + " " + direction);
            }

            selectSql.append(String.join(", ", orderClauses));
        }

        selectSql.append(" LIMIT ?, ?");
        params.add(filter.getPage());
        params.add(filter.getLimit());

        List<AdminMovieResponseDTO> content;

        try {
            content = executor.executeNativeQuery(
                    selectSql.toString(),
                    params,
                    AdminMovieResponseDTO.class
            );
        }catch (Exception ex) {
            throw new RuntimeException("Lỗi khi chạy query movie: " + ex.getMessage(), ex);
        }

        String countSql = """
                    SELECT COUNT(*) FROM (
                        SELECT 1 FROM tbl_movies m
                """ +
                whereClause + ") c";

        Query countQ = entityManager.createNativeQuery(countSql);
        for (int i = 0; i < params.size() - 2; i++) {
            countQ.setParameter(i + 1, params.get(i));
        }

        Number totalNum = (Number) countQ.getSingleResult();
        long total = totalNum.longValue();


        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<AdminMovieResponseDTO> findAll(AdminMoviesFilterDto filter) {
        StringBuilder selectSql = new StringBuilder("""
            SELECT 
                m.MOVIE_ID,
                m.CASTS,
                m.CREATED_AT,
                m.DESCRIPTION,
                m.DIRECTOR,
                m.EPISODE_NUMBER,
                m.MOVIES,
                m.NAME,
                m.ORIGINAL_NAME,
                m.POSTER_URL,
                m.QUALITY,
                m.RATING_AVG,
                m.RELEASE_DATE,
                m.SLUG,
                m.SOURCES,
                m.THUMB_URL,
                m.TIME,
                m.TRAILER_URL,
                m.VIEWS
            FROM tbl_movies m        
        """);

        StringBuilder whereClause = new StringBuilder("WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Tìm theo tên, original_name
        if(StringUtils.hasText(filter.getName())) {
            whereClause.append(" AND m.NAME LIKE ? OR m.ORIGINAL_NAME LIKE ?");
            params.add("%"+filter.getName()+"%");
            params.add("%"+filter.getName()+"%");
        }

        if (filter.getGenre() != null && !filter.getGenre().isEmpty()) {
            Set<String> set = Arrays.stream(filter.getGenre().split(","))
                    .map(String::trim).filter(s->!s.isEmpty())
                    .collect(Collectors.toSet());
            if (!set.isEmpty()) {
                String inClause = set.stream().map(s->"?").collect(Collectors.joining(","));
                whereClause.append(" AND t.CURRENCY_CODE IN (").append(inClause).append(")");
                params.addAll(set);
            }
        }

        selectSql.append(whereClause);
        selectSql.append(" ORDER BY ID, DESC");

        selectSql.append(" LIMIT ?, ?");
        params.add(filter.getPage(), filter.getLimit());

        List<AdminMovieResponseDTO> content;

        try {
            content = executor.executeNativeQuery(
                    selectSql.toString(),
                    params,
                    AdminMovieResponseDTO.class
            );
            return content;
        }catch (Exception ex) {
            throw new RuntimeException("Lỗi khi chạy query movie: " + ex.getMessage(), ex);
        }


    }
}
