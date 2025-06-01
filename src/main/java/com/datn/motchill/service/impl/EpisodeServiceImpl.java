package com.datn.motchill.service.impl;

import com.datn.motchill.common.exceptions.BadRequestException;
import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.common.utils.Constants;
import com.datn.motchill.common.utils.ObjectMapperUtils;
import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.episode.EpisodeDto;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.service.FileStorageService;
import com.datn.motchill.util.SlugUtils;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.datn.motchill.dto.episode.EpisodeFilterDTO;
import com.datn.motchill.entity.Episode;
import com.datn.motchill.repository.EpisodeRepository;
import com.datn.motchill.repository.MovieRepository;
import com.datn.motchill.service.EpisodeService;
import com.datn.motchill.service.VideoProcessingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class EpisodeServiceImpl implements EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final MovieRepository movieRepository;
    private final VideoProcessingService videoProcessingService;
    private static final String DATA_FOLDER = "uploads/videos";
    private final ModelMapper modelMapper;
    private String ffmpegPath = "ffmpeg";


    private final FileStorageService fileStorageService;
    
    private String hlsDir = "uploads/videos/output/hls/adaptive_video";

    @Override
    public Page<EpisodeDto> searchAdmin(EpisodeFilterDTO filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit());
        Specification<Episode> specification = this.getSearchSpecification(filter);
        return episodeRepository.findAll(specification, pageable).map(episode ->
            ObjectMapperUtils.map(episode, EpisodeDto.class)
        );
    }

    @Override
    public EpisodeDto findById(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tập phim không tồn tại với ID: " + id));
        return ObjectMapperUtils.map(episode, EpisodeDto.class);
    }

    @Override
    public EpisodeDto getEpisodeByMovieIdAndSlug(Long movieId, String slug) {
        Episode episode = episodeRepository.findByMovieIdAndSlug(movieId, slug)
                .orElseThrow(() -> new NotFoundException("Tập phim không tồn tại"));
        return ObjectMapperUtils.map(episode, EpisodeDto.class);
    }

    @Override
    public EpisodeDto updateEpisode(Long id, MultipartFile file, EpisodeDto episodeDto) throws IOException {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tập phim với ID: " + episodeDto.getId()));

        Movie movie = movieRepository.findById(episodeDto.getMovieId())
                .orElseThrow(() -> new NotFoundException("Phim không tồn tại với ID: " + episodeDto.getMovieId()));
        // Đảm bảo thư mục lưu trữ tồn tại
        File uploadDir = new File(DATA_FOLDER);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        Integer episodeNumber = episodeRepository.countByMovieId(episodeDto.getMovieId());
        // Lưu file gốc
        String slug;
        if(StringUtils.hasText(episodeDto.getName())) {
            episodeDto.setName(episodeDto.getName().trim());
            slug = SlugUtils.slugify(episodeDto.getName());
        } else {
            episodeDto.setName(String.valueOf(episodeNumber));
            slug = "tap-" + episodeNumber;
        }

        String fileName = StringUtils.cleanPath(slug + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        Path storageFolder = Paths.get(DATA_FOLDER + "/" + movie.getId());
        if (!Files.exists(storageFolder)) {
            Files.createDirectories(storageFolder);
        }
        Path filePath = storageFolder.resolve(fileName);

        // Lưu file vào đường dẫn
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Tạo thư mục đầu ra HLS nếu không tồn tại
        Path hlsPath = Paths.get(hlsDir);
        if (!Files.exists(hlsPath)) {
            Files.createDirectories(hlsPath);
        }

        // Lưu episode vào database và trả về cho người dùng trạng thái xử lý tập phim là đang xử lý
        modelMapper.map(episodeDto, episode);
        episode.setSlug(slug);
        episode.setEpisodeNumber(episodeNumber);
        episode.setMovie(movie);
        episode.setVideoPath(filePath.toString());
        episode.setStatus("processing"); // Đặt trạng thái đang xử lý
        Episode savedEpisode = episodeRepository.save(episode);

        // Lưu episode vào database
        EpisodeDto result = ObjectMapperUtils.map(savedEpisode, EpisodeDto.class);

        // Commit transaction trước khi bắt đầu xử lý bất đồng bộ
        // Commit transaction trước khi bắt đầu xử lý bất đồng bộ
        TransactionSynchronizationManager.registerSynchronization(
                new AfterCommitCallback(() ->
                        videoProcessingService.processVideoAsync(filePath.toString(), savedEpisode.getId())
                )
        );

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EpisodeDto saveEpisode(MultipartFile file, EpisodeDto episodeDto) throws IOException {
        Movie movie = movieRepository.findById(episodeDto.getMovieId())
                .orElseThrow(() -> new NotFoundException("Phim không tồn tại với ID: " + episodeDto.getMovieId()));
        // Đảm bảo thư mục lưu trữ tồn tại
        File uploadDir = new File(DATA_FOLDER);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        Integer episodeNumber = episodeRepository.countByMovieId(episodeDto.getMovieId());
        // Lưu file gốc
        String slug;
        if(StringUtils.hasText(episodeDto.getName())) {
            episodeDto.setName(episodeDto.getName().trim());
            slug = SlugUtils.slugify(episodeDto.getName());
        } else {
            episodeDto.setName(String.valueOf(episodeNumber));
            slug = "tap-" + episodeNumber;
        }

        String fileName = StringUtils.cleanPath(slug + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        Path storageFolder = Paths.get(DATA_FOLDER + "/" + movie.getId());
        if (!Files.exists(storageFolder)) {
            Files.createDirectories(storageFolder);
        }
        Path filePath = storageFolder.resolve(fileName);

        // Lưu file vào đường dẫn
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Tạo thư mục đầu ra HLS nếu không tồn tại
        Path hlsPath = Paths.get(hlsDir);
        if (!Files.exists(hlsPath)) {
            Files.createDirectories(hlsPath);
        }

        // Lưu episode vào database và trả về cho người dùng trạng thái xử lý tập phim là đang xử lý
        Episode episode = modelMapper.map(episodeDto, Episode.class);
        episode.setSlug(slug);
        episode.setEpisodeNumber(episodeNumber);
        episode.setMovie(movie);
        episode.setVideoPath(filePath.toString());
        episode.setStatus("processing"); // Đặt trạng thái đang xử lý
        Episode savedEpisode = episodeRepository.save(episode);

        // Lưu episode vào database
        EpisodeDto result = ObjectMapperUtils.map(savedEpisode, EpisodeDto.class);

        // Commit transaction trước khi bắt đầu xử lý bất đồng bộ
        // Commit transaction trước khi bắt đầu xử lý bất đồng bộ
        TransactionSynchronizationManager.registerSynchronization(
                new AfterCommitCallback(() ->
                        videoProcessingService.processVideoAsync(filePath.toString(), savedEpisode.getId())
                )
        );

        return result;
    }

    @Override
    public String delete(AdminIdsDTO idsDto) {
        List<Long> listIds = Arrays.stream(idsDto.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toUnmodifiableList());
        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<Episode> episodes = episodeRepository.findAllById(listIds);

        if (ObjectUtils.isEmpty(episodes)) {
            throw new NotFoundException(Constants.KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON);
        }

        // Kiểm tra trạng thái của tập phim nữa


        episodeRepository.deleteAllById(listIds);
        return String.format(Constants.XOA_THANH_CONG_XXX_BAN_GHI, listIds.size());
    }

    @Transactional
    @Override
    public void deleteAllByMovieId(AdminIdsDTO movieIds) {
        List<Long> listIds = Arrays.stream(movieIds.getIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toUnmodifiableList());
        if (ObjectUtils.isEmpty(listIds)) {
            throw new BadRequestException(Constants.DANH_SACH_ID_KHONG_DUOC_DE_TRONG);
        }

        List<Episode> episodes = episodeRepository.findAllByMovieIdIn(listIds);

        for (Episode episode : episodes) {
            deleteEpisodeFilesSafely(episode);
        }

        // Xóa thư mục chứa video theo từng movieId trong danh sách
        for (Long movieId : listIds) {
            Path movieFolder = Paths.get(DATA_FOLDER, String.valueOf(movieId));
            try {
                if (Files.exists(movieFolder)) {
                    fileStorageService.deleteDirectoryRecursively(movieFolder);
                }
            } catch (IOException e) {
                log.warn("Không thể xóa thư mục video của phim ID: {}. Tiếp tục xóa dữ liệu DB.", movieId, e);
            }
        }

        // Xóa tập phim theo batch
        episodeRepository.deleteAllInBatch(episodes);

        // Xóa phim tương ứng với từng movieId
        // Ở đây cần xử lý xóa từng phim theo id, giả sử movieRepository có phương thức findById
        for (Long movieId : listIds) {
            movieRepository.findById(movieId).ifPresent(movieRepository::delete);
        }
    }

    private void deleteEpisodeFilesSafely(Episode episode) {
        try {
            Optional.ofNullable(episode.getVideoPath())
                    .map(Paths::get)
                    .ifPresent(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.error("Không thể xóa video mp4 của tập ID: {}", episode.getId(), e);
                        }
                    });

            Optional.ofNullable(episode.getHlsFolder())
                    .map(Paths::get)
                    .filter(Files::exists)
                    .ifPresent(folder -> {
                        try {
                            fileStorageService.deleteDirectoryRecursively(folder);
                        } catch (IOException e) {
                            log.error("Không thể xóa thư mục HLS của tập ID: {}", episode.getId(), e);
                        }
                    });

        } catch (Exception e) {
            log.error("Lỗi không xác định khi xử lý file của tập ID: {}", episode.getId(), e);
        }
    }

    private Specification<Episode> getSearchSpecification(final EpisodeFilterDTO request) {
        return new Specification<>() {

            private static final long serialVersionUID = 6345534328548406667L;

            @Override
            @Nullable
            public Predicate toPredicate(Root<Episode> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(request.getSearch() != null) {
                    predicates.add(cb.like(root.get("name"), "%" + request.getSearch() + "%"));
                }

                if (request.getMovieId() != null) {
                    predicates.add(cb.equal(root.get("movie").get("id"), request.getMovieId()));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
    }

    // Tạo một class helper để chỉ override afterCommit
    private static class AfterCommitCallback implements TransactionSynchronization {
        private final Runnable callback;
        
        public AfterCommitCallback(Runnable callback) {
            this.callback = callback;
        }
        
        @Override
        public void afterCommit() {
            callback.run();
        }
        
        @Override public void afterCompletion(int status) {}
        @Override public void beforeCommit(boolean readOnly) {}
        @Override public void beforeCompletion() {}
        @Override public void suspend() {}
        @Override public void resume() {}
        @Override public void flush() {}
    }
}
