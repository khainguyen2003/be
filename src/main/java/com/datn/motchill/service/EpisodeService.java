package com.datn.motchill.service;

import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.episode.EpisodeDto;
import com.datn.motchill.dto.episode.EpisodeFilterDTO;
import com.datn.motchill.dto.episode.EpisodeRequest;
import com.datn.motchill.entity.Episode;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.examples.videoProcessing.Mp4ToHlsConverter;
import com.datn.motchill.exception.EntityNotFoundException;
import com.datn.motchill.exception.FileProcessingException;
import com.datn.motchill.exception.ServiceException;
import com.datn.motchill.repository.EpisodeRepository;
import com.datn.motchill.repository.MovieRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface EpisodeService {
    /**
     * Tìm tập phim theo ID
     */
    EpisodeDto findById(Long id);

    String delete(AdminIdsDTO idsDto);

    Page<EpisodeDto> searchAdmin(EpisodeFilterDTO filter);

    EpisodeDto getEpisodeByMovieIdAndSlug(Long movieId, String slug);

    EpisodeDto updateEpisode(Long id, MultipartFile file, EpisodeDto episodeDto) throws IOException;

    EpisodeDto saveEpisode(MultipartFile file, EpisodeDto episodeDto) throws IOException;

    void deleteAllByMovieId(AdminIdsDTO movieIds);
}


