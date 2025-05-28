package com.datn.motchill.service;

import com.datn.motchill.dto.genre.GenreDto;
import com.datn.motchill.dto.genre.GenreRequest;
import com.datn.motchill.entity.Genre;
import com.datn.motchill.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreDto> getAll() {
        return genreRepository.findAll().stream()
                .map(g -> new GenreDto(g.getId(), g.getName(), g.getSlug()))
                .collect(Collectors.toList());
    }

    public GenreDto getById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại"));
        return new GenreDto(genre.getId(), genre.getName(), genre.getSlug());
    }

    public GenreDto create(GenreRequest request) {
        Genre genre = new Genre();
        if(genreRepository.isExist(request.getName(), request.getSlug())) {
            throw new RuntimeException("Thể loại đã tồn tại");
        }
        genre.setName(request.getName());
        genre.setSlug(request.getSlug());
        genreRepository.save(genre);
        return new GenreDto(genre.getId(), genre.getName(), genre.getSlug());
    }

    public GenreDto update(Long id, GenreRequest request) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại"));
        if(genreRepository.isExistAndIdNot(request.getName(), request.getSlug(), id)) {
            throw new RuntimeException("Thể loại đã tồn tại");
        }
        genre.setName(request.getName());
        genre.setSlug(request.getSlug());
        genreRepository.save(genre);
        return new GenreDto(genre.getId(), genre.getName(), genre.getSlug());
    }

    public void delete(Long id) {
        genreRepository.deleteById(id);
    }
}

