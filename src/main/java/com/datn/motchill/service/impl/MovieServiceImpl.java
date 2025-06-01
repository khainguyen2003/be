package com.datn.motchill.service.impl;

import com.datn.motchill.common.exceptions.BadRequestException;
import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.common.utils.Constants;
import com.datn.motchill.dto.movie.MovieDto;
import com.datn.motchill.dto.movie.MovieFilterDTO;
import com.datn.motchill.dto.movie.MovieRequest;
import com.datn.motchill.entity.Country;
import com.datn.motchill.entity.Genre;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.entity.Tag;
import com.datn.motchill.enums.MovieStatusEnum;
import com.datn.motchill.enums.MovieTypeEnum;
import com.datn.motchill.repository.CountryRepository;
import com.datn.motchill.repository.DirectorRepository;
import com.datn.motchill.repository.EpisodeRepository;
import com.datn.motchill.repository.GenreRepository;
import com.datn.motchill.repository.MovieRepository;
import com.datn.motchill.repository.TagRepository;
import com.datn.motchill.repository.ViewStatisticsRepository;
import com.datn.motchill.service.MovieService;
import com.datn.motchill.util.SlugUtils;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;
    private final TagRepository tagRepository;
    private final EpisodeRepository episodeRepository;
    private final ViewStatisticsRepository viewStatisticsRepository;
    private final DirectorRepository directorRepository;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Page<MovieDto> searchAdmin(MovieFilterDTO filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit(), Sort.by("id").descending());
        Specification<Movie> specification = getSearchSpecification(filter);

        return movieRepository.findAll(specification, pageable).map((element) -> modelMapper.map(element, MovieDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto findById(Long id) {
        return movieRepository.findById(id)
                .map((element) -> modelMapper.map(element, MovieDto.class))
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto findBySlug(String slug) {
        return movieRepository.findBySlug(slug)
                .map((element) -> modelMapper.map(element, MovieDto.class))
                .orElseThrow(() -> new NotFoundException("Movie not found with slug: " + slug));
    }

    @Override
    public MovieDto saveDraft(MovieRequest request) {
        // kiểm tra tồn tại
        if(movieRepository.existsByName(request.getName())) {
            throw new BadRequestException("Phim đã tồn tại");
        }

        // Validate thể loại phim
        Set<Long> genreIds = request.getGenreIds();
        List<Genre> genres = genreRepository.findAllById(genreIds);

        // validate
        Set<Long> foundIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Set<Long> invalidIds = new HashSet<>(genreIds);
        invalidIds.removeAll(foundIds);

        if (!invalidIds.isEmpty()) {
            throw new BadRequestException("Thể loại không tồn tại: " + invalidIds);
        }

        Movie movie = modelMapper.map(request, Movie.class);
        
        // Generate slug if not provided
        if (movie.getSlug() == null || movie.getSlug().isBlank()) {
            movie.setSlug(SlugUtils.slugify(movie.getName()));
        }
        
        // Default status if not provided
        movie.setStatus(MovieStatusEnum.SAVE_DRAFT);
        movie.setGenres(genres);
        
        movie = movieRepository.save(movie);
        
        return modelMapper.map(movie, MovieDto.class);
    }

    @Override
    public MovieDto updateDraft(Long id, MovieRequest request) {
        if (id == null) {
            throw new BadRequestException(Constants.ID_KHONG_DUOC_DE_TRONG);
        }

        Movie foundEntity = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phim với id: " + id));

        // Validate thể loại phim
        // kiểm tra tồn tại
        if(movieRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("Phim đã tồn tại");
        }

        validateRequest(request);

        // Generate slug if not provided
        if (foundEntity.getSlug() == null || foundEntity.getSlug().isBlank()) {
            foundEntity.setSlug(SlugUtils.slugify(foundEntity.getName()));
        }

        List<Genre> genres = genreRepository.findAllById(request.getGenreIds());
        modelMapper.map(request, foundEntity);
        movieRepository.save(foundEntity);

        // Generate slug if not provided
        if (foundEntity.getSlug() == null || foundEntity.getSlug().isBlank()) {
            foundEntity.setSlug(SlugUtils.slugify(foundEntity.getName()));
        }

        // Default status if not provided
        foundEntity.setStatus(MovieStatusEnum.SAVE_DRAFT);
        foundEntity.setGenres(genres);

        foundEntity = movieRepository.save(foundEntity);

        return modelMapper.map(foundEntity, MovieDto.class);
    }

    public void validateRequest(MovieRequest request) {

        // Validate thể loại phim
        Set<Long> genreIds = request.getGenreIds();
        List<Genre> genres = genreRepository.findAllById(genreIds);

        // validate
        Set<Long> foundIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Set<Long> invalidIds = new HashSet<>(genreIds);
        invalidIds.removeAll(foundIds);

        if (!invalidIds.isEmpty()) {
            throw new BadRequestException("Thể loại không tồn tại: " + invalidIds);
        }
    }

    @Override
    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));
        
        // Delete related episodes first
        episodeRepository.deleteByMovie(movie);
        
        // Delete the movie
        movieRepository.delete(movie);
    }

    @Override
    @Transactional
    public MovieDto updateStatus(Long id, MovieStatusEnum status) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));
        
        movie.setStatus(status);
        movie = movieRepository.save(movie);
        
        return modelMapper.map(movie, MovieDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovieDto> findByType(MovieTypeEnum type, Pageable pageable) {
        Page<Movie> movies = movieRepository.findByMovieType(type, pageable);
        return movies.map(dto -> modelMapper.map(dto, MovieDto.class));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MovieDto> findByStatus(MovieStatusEnum status, Pageable pageable) {
        Page<Movie> movies = movieRepository.findByStatus(status, pageable);
        return movies.map(dto -> modelMapper.map(dto, MovieDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovieDto> findByGenreId(Long genreId, Pageable pageable) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Genre not found with id: " + genreId));
        
        return movieRepository.findByGenresContaining(genre, pageable)
                .map(dto -> modelMapper.map(dto, MovieDto.class));
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<MovieDto> findByCountryId(Long countryId, Pageable pageable) {
//        Country country = countryRepository.findById(countryId)
//                .orElseThrow(() -> new NotFoundException("Country not found with id: " + countryId));
//
//        return movieRepository.findByCountriesContaining(country, pageable)
//                .map(dto -> modelMapper.map(dto, MovieDto.class));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<MovieDto> findByTagId(Long tagId, Pageable pageable) {
//        Tag tag = tagRepository.findById(tagId)
//                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + tagId));
//
//        return movieRepository.findByTagsContaining(tag, pageable)
//                .map(dto -> modelMapper.map(dto, MovieDto.class));
//    }

    /**
     * Tìm phim đặc sắc
     * @param limit
     * @return
     */
    @Override
    public List<MovieDto> findFeatured(int limit) {
        return List.of();
    }

    @Override
    public Page<MovieDto> search(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> findLatest(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return movieRepository.findByStatus(MovieStatusEnum.PUBLISHED, pageable)
                .stream()
                .map(dto -> modelMapper.map(dto, MovieDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> findMostViewed(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        
        return viewStatisticsRepository.findAllOrderByTotalViewsDesc(pageable)
                .stream()
                .map(vs -> {
                    MovieDto dto = modelMapper.map(vs, MovieDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Specification<Movie> getSearchSpecification(final MovieFilterDTO request) {
        return new Specification<>() {

            private static final long serialVersionUID = 6345534328548406667L;

            @Override
            @Nullable
            public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if(request.getKeyword() != null) {
                    String search = "%" + request.getKeyword().toLowerCase() + "%";
                    Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), search);
                    Predicate originalNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("originalName")), search);

                    predicates.add(criteriaBuilder.or(namePredicate, originalNamePredicate));
                }

                if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
                    Join<Movie, Genre> genreJoin = root.join("genres", JoinType.INNER);
                    predicates.add(genreJoin.get("id").in(request.getGenreIds()));
                    query.distinct(true); // vì join có thể tạo duplicate row
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
    }
}
