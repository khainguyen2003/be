package com.datn.motchill.service;

import com.datn.motchill.admin.common.exceptions.BadRequestException;
import com.datn.motchill.dto.movie.MovieRequestDto;
import com.datn.motchill.dto.movie.MovieResponseDto;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.repository.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public MovieResponseDto createMovie(MovieRequestDto dto) {
        Movie movie = objectMapper.convertValue(dto, Movie.class);

        // Fetch related entities by ids
        movie.setCasts(actorRepository.findAllById(dto.getCastIds()));
        movie.setDirectors(directorRepository.findAllById(dto.getDirectorIds()));
        movie.setGenres(genreRepository.findAllById(dto.getGenreIds()));
//        movie.setCountry(countryRepository.findById(dto.getCountryId())
//                .orElseThrow(() -> new RuntimeException("Country không tồn tại")));

        movieRepository.save(movie);

        // Convert back entity to dto (gán id tự động)
        return objectMapper.convertValue(movie, MovieResponseDto.class);
    }

    @Transactional
    public MovieResponseDto updateMovie(Long id, MovieRequestDto dto) {
        if(id == null) {
            throw new BadRequestException("Phim không tồn tại");
        }

        try {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Phim không tồn tại"));

            // Map DTO sang entity (cập nhật các trường, ngoại trừ id)
            objectMapper.updateValue(movie, dto);

            // Cập nhật các quan hệ ManyToMany & ManyToOne
            movie.setCasts(actorRepository.findAllById(dto.getCastIds()));
            movie.setDirectors(directorRepository.findAllById(dto.getDirectorIds()));
            movie.setGenres(genreRepository.findAllById(dto.getGenreIds()));
//            movie.setCountry(countryRepository.findById(dto.getCountryId())
//                    .orElseThrow(() -> new RuntimeException("Country không tồn tại")));

            movieRepository.save(movie);

            return objectMapper.convertValue(movie, MovieResponseDto.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }


    }

    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Phim không tồn tại");
        }
        movieRepository.deleteById(id);
    }

    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movie -> objectMapper.convertValue(movie, MovieResponseDto.class))
                .toList();
    }

    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại"));
        return objectMapper.convertValue(movie, MovieResponseDto.class);
    }
}
