package com.datn.motchill.controller;

import com.datn.motchill.admin.presentation.dto.episodes.EpisodeResponseDTO;
import com.datn.motchill.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
public class EpisodeController {
    private final EpisodeService episodeService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EpisodeResponseDTO> uploadEpisode(
            @RequestParam("file") MultipartFile file,
            @RequestParam("movieId") Long movieId,
            @RequestParam("name") String name) throws IOException {

        EpisodeResponseDTO episode = episodeService.saveEpisode(file, movieId, name);
        return ResponseEntity.ok(episode);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        episodeService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
