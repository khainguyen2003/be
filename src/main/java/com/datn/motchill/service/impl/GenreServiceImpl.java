package com.datn.motchill.service.impl;

import com.datn.motchill.common.exceptions.BadRequestException;
import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.common.utils.Constants;
import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.genre.GenreDto;
import com.datn.motchill.dto.genre.GenreRequest;
import com.datn.motchill.dto.OptionDTO;
import com.datn.motchill.dto.genre.GenreFilter;
import com.datn.motchill.entity.Genre;
import com.datn.motchill.repository.GenreRepository;
import com.datn.motchill.repository.MovieRepository;
import com.datn.motchill.service.GenreService;
import com.datn.motchill.util.SlugUtils;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<GenreDto> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable)
                .map(item -> modelMapper.map(item, GenreDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenreDto> search(GenreFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit());

        Specification<Genre> specification = getSearchSpecification(filter);

        return genreRepository.findAll(specification, pageable)
                .map(item -> modelMapper.map(item, GenreDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(item -> modelMapper.map(item, GenreDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDTO> findAllOptions() {
        return genreRepository.findAll().stream()
                .map(genre -> new OptionDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDto findById(Long id) {
        return genreRepository.findById(id)
                .map(item -> modelMapper.map(item, GenreDto.class))
                .orElseThrow(() -> new NotFoundException("Genre not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDto findBySlug(String slug) {
        return genreRepository.findBySlug(slug)
                .map(item -> modelMapper.map(item, GenreDto.class))
                .orElseThrow(() -> new NotFoundException("Genre not found with slug: " + slug));
    }

    @Override
    public GenreDto create(GenreRequest request) {
        Genre genre = new Genre();
        // check trùng
        if(genreRepository.existsByName(request.getName())) {
            throw new BadRequestException("Danh mục phim đã tồn tại");
        }

        updateGenreFromRequest(genre, request);
        
        // Generate slug if not provided
        if (genre.getSlug() == null || genre.getSlug().isBlank()) {
            genre.setSlug(SlugUtils.slugify(genre.getName()));
        }
        
        genre = genreRepository.save(genre);
        
        return modelMapper.map(genre, GenreDto.class);
    }

    @Override
    public GenreDto update(Long id, GenreRequest request) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Thể loại phim không tồn tại: " + id));

        if(genreRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("Danh mục phim đã tồn tại");
        }

        updateGenreFromRequest(genre, request);
        
        // Generate slug if not provided
        if (genre.getSlug() == null || genre.getSlug().isBlank()) {
            genre.setSlug(SlugUtils.slugify(genre.getName()));
        }
        
        genre = genreRepository.save(genre);
        
        return modelMapper.map(genre, GenreDto.class);
    }

    /**
     *
     * @param idsDto các id được nối với nhau bằng dấu , id1,id2
     * @return
     */
    @Override
    public String delete(AdminIdsDTO idsDto) {
        List<Long> listIds = Arrays.stream(idsDto.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toUnmodifiableList());
        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<Genre> genreList = genreRepository.findAllById(listIds);
        if (ObjectUtils.isEmpty(genreList)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        // check đang dùng bởi phim. nếu có thì bỏ qua không xóa
        List<Long> idValid = new ArrayList<>();
        for (Genre genre : genreList) {
            boolean isUsed = movieRepository.existsByGenresContaining(genre); // giả sử mối quan hệ là Movie -> List<Genre>
            if (!isUsed) {
                idValid.add(genre.getId());
            }
        }

        if (!idValid.isEmpty()) {
            genreRepository.deleteAllByIdInBatch(idValid);
        }

        return String.format("Xóa thành công %d/%d danh mục phim!", idValid.size(), listIds.size());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        return genreRepository.existsBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return genreRepository.existsByName(name);
    }
    
    private void updateGenreFromRequest(Genre genre, GenreRequest request) {
        genre.setName(request.getName());
        
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            genre.setSlug(request.getSlug());
        }
    }
    
    private GenreDto convertToDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName(),
                genre.getSlug()
        );
    }


    private Specification<Genre> getSearchSpecification(final GenreFilter request) {
        return new Specification<>() {

            private static final long serialVersionUID = 6345534328548406667L;

            @Override
            @Nullable
            public Predicate toPredicate(Root<Genre> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(request.getSearch() != null) {
                    predicates.add(cb.like(root.get("name"), "%" + request.getSearch() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
    }
}
