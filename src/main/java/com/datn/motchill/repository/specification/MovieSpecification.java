package com.datn.motchill.repository.specification;

import com.datn.motchill.dto.movie.MovieFilterDTO;
import com.datn.motchill.entity.Country;
import com.datn.motchill.entity.Director;
import com.datn.motchill.entity.Genre;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.entity.Tag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecification {
    
    /**
     * Build specification for movie filtering
     */
    public static Specification<Movie> getMovieSpecification(MovieFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Tìm kiếm theo từ khóa (title, description)
            if (StringUtils.hasText(filter.getKeyword())) {
                String keyword = "%" + filter.getKeyword().toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), 
                        keyword);
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), 
                        keyword);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }
            
            // Lọc theo năm phát hành
            if (filter.getReleaseYear() != null) {
                predicates.add(criteriaBuilder.equal(root.get("releaseYear"), filter.getReleaseYear()));
            }
            
            // Lọc theo loại phim (phim lẻ, phim bộ)
            if (filter.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            }
            
            // Lọc theo trạng thái phim (hoàn thành, đang cập nhật)
            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }
            
            // Lọc theo thể loại
            if (filter.getGenreIds() != null && !filter.getGenreIds().isEmpty()) {
                Join<Movie, Genre> genreJoin = root.join("genres", JoinType.INNER);
                predicates.add(genreJoin.get("id").in(filter.getGenreIds()));
            }
            
            // Lọc theo quốc gia
            if (filter.getCountryIds() != null && !filter.getCountryIds().isEmpty()) {
                Join<Movie, Country> countryJoin = root.join("countries", JoinType.INNER);
                predicates.add(countryJoin.get("id").in(filter.getCountryIds()));
            }
            
            // Lọc theo đạo diễn
            if (filter.getDirectorIds() != null && !filter.getDirectorIds().isEmpty()) {
                Join<Movie, Director> directorJoin = root.join("directors", JoinType.INNER);
                predicates.add(directorJoin.get("id").in(filter.getDirectorIds()));
            }
            
            // Lọc theo tag
            if (filter.getTagIds() != null && !filter.getTagIds().isEmpty()) {
                Join<Movie, Tag> tagJoin = root.join("tags", JoinType.INNER);
                predicates.add(tagJoin.get("id").in(filter.getTagIds()));
            }
            
            // Ngăn chặn trùng lặp kết quả khi sử dụng nhiều join
            query.distinct(true);
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
