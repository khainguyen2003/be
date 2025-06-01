package com.datn.motchill.controller.admin;

import com.datn.motchill.dto.AdminBaseResponse;
import com.datn.motchill.dto.AdminIdsDTO;
import com.datn.motchill.dto.episode.EpisodeDto;
import com.datn.motchill.dto.episode.EpisodeFilterDTO;
import com.datn.motchill.service.EpisodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/episodes")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminEpisodeController {

    private final EpisodeService episodeService;
    private final ObjectMapper objectMapper;
    private static final String SUCCESS_KEY = "success";
    private static final String MESSAGE_KEY = "message";


    private String ffmpegPath = "ffmpeg";

    private String uploadDir = "uploads/videos";

    private String hlsDir = "uploads/videos/output/hls/adaptive_video";

    /**
     * Lấy danh sách tập phim theo phim với phân trang
     */
    @GetMapping("/search")
    public ResponseEntity<Page<EpisodeDto>> getAllEpisodesByMovieId(
            EpisodeFilterDTO filterDto) {
        return ResponseEntity.ok(episodeService.searchAdmin(filterDto));
    }

//    /**
//     * Lấy danh sách tất cả tập phim của một phim
//     */
//    @GetMapping("/movie/{movieId}/all")
//    public ResponseEntity<List<EpisodeDto>> getAllEpisodesByMovieId(@PathVariable Long movieId) {
//        return ResponseEntity.ok(episodeService.findAllByMovieId(movieId));
//    }

    /**
     * Lấy thông tin tập phim theo id
     */
    @GetMapping("/{id}")
    public ResponseEntity<EpisodeDto> getEpisodeById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.findById(id));
    }

    /**
     * Lấy thông tin tập phim theo phim và số tập
     */
    @GetMapping("/movie/{movieId}/number/{slug}")
    public ResponseEntity<EpisodeDto> getEpisodeByMovieIdAndSlug(
            @PathVariable Long movieId,
            @PathVariable String slug) {
        return ResponseEntity.ok(episodeService.getEpisodeByMovieIdAndSlug(movieId, slug));
    }

    /**
     * Tạo tập phim mới
     */
    @PostMapping(value = "/save-draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EpisodeDto> createEpisode(
        @RequestParam("file") MultipartFile file,
        @RequestParam("movieId") Long movieId,
        @RequestParam("episodeJson") String episodeJson
    ) throws IOException {
        EpisodeDto createRequest = objectMapper.readValue(episodeJson, EpisodeDto.class);
        createRequest.setMovieId(movieId);
        EpisodeDto episode = episodeService.saveEpisode(file, createRequest);
        return ResponseEntity.ok(episode);
    }

    /**
     * Cập nhật tập phim
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EpisodeDto> updateEpisode(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("movieId") Long movieId,
            @RequestParam("episodeJson") String episodeJson
    ) throws IOException {
        EpisodeDto episodeDto = objectMapper.readValue(episodeJson, EpisodeDto.class);
        episodeDto.setMovieId(movieId);
        episodeDto.setId(id);
        EpisodeDto episode = episodeService.updateEpisode(id, file, episodeDto);
        return ResponseEntity.ok(episode);
    }

    /**
     * Xóa tập phim
     */
    @PostMapping("/delete")
    public ResponseEntity<AdminBaseResponse> deleteEpisode(@RequestBody AdminIdsDTO idsDTO) {
        String result = episodeService.delete(idsDTO);
        AdminBaseResponse response = new AdminBaseResponse().success(result);
        return ResponseEntity.ok(response);
    }

//    /**
//     * Xóa tất cả tập phim của một phim
//     */
//    @DeleteMapping("/episode")
//    public ResponseEntity<AdminBaseResponse> deleteAllEpisodesByMovieId(@PathVariable AdminIdsDTO idsDTO) {
//        String response = episodeService.delete(idsDTO);
//        return ResponseEntity.noContent().build();
//    }
}
