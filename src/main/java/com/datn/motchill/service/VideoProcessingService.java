package com.datn.motchill.service;

import com.datn.motchill.common.exceptions.BadRequestException;
import com.datn.motchill.entity.Episode;
import com.datn.motchill.examples.videoProcessing.Mp4ToHlsConverter;
import com.datn.motchill.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingService {

    private final EpisodeRepository episodeRepository;
    
    // Có thể cấu hình từ application.yml
    private String ffmpegPath = "ffmpeg";
    private String hlsDir = "uploads/videos/output/hls/adaptive_video";

    /**
     * Cập nhật trạng thái tập phim trong transaction riêng biệt
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateEpisodeStatus(Long episodeId, String status, String videoUrl, String hlsFolder) {
        try {
            Optional<Episode> episodeOpt = episodeRepository.findById(episodeId);
            if (episodeOpt.isPresent()) {
                Episode episode = episodeOpt.get();
                
                if (videoUrl != null) {
                    episode.setVideoUrl(videoUrl);
                }
                
                if (hlsFolder != null) {
                    episode.setHlsFolder(hlsFolder);
                }
                
                episode.setStatus(status);
                episode.setUpdatedDate(new Date());
                episodeRepository.save(episode);
                
                log.info("Đã cập nhật trạng thái tập phim ID: {} thành: {}", episodeId, status);
            } else {
                log.warn("Không tìm thấy tập phim với ID: {} để cập nhật trạng thái", episodeId);
            }
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái tập phim: {}", e.getMessage());
        }
    }
    
    /**
     * Xử lý video MP4 thành HLS bất đồng bộ
     * @param videoPath Đường dẫn đến file MP4
     * @param episodeId ID của tập phim
     */
    public void processVideoAsync(String videoPath, Long episodeId) {

        // Tạo thread riêng để xử lý video
        new Thread(() -> {
            try {
                log.info("Bắt đầu xử lý video cho tập phim ID: {}", episodeId);
                
                // Cập nhật trạng thái đang xử lý trong transaction riêng
                updateEpisodeStatus(episodeId, "processing", null, null);

                // Lấy thông tin tập phim
                Episode episode = episodeRepository.findById(episodeId)
                 .orElseThrow(() -> new RuntimeException("Không tìm thấy tập phim với ID: " + episodeId));

                // Tạo cấu trúc thư mục phân cấp: movieId/episodeSlug
                Long movieId = episode.getMovie().getId();
                String episodeSlug = episode.getSlug();
                
                // Tạo thư mục đầu ra theo ID phim
                String movieOutputDir = hlsDir + "/" + movieId;
                Path movieDirPath = Paths.get(movieOutputDir);
                if (!Files.exists(movieDirPath)) {
                    Files.createDirectories(movieDirPath);
                }
                
                // Thư mục cho tập phim cụ thể
                String episodeOutputDir = movieOutputDir + "/" + episodeSlug;
                Path episodeDirPath = Paths.get(episodeOutputDir);
                if (!Files.exists(episodeDirPath)) {
                    Files.createDirectories(episodeDirPath);
                }
                
                // Khởi tạo converter
                Mp4ToHlsConverter converter = new Mp4ToHlsConverter(ffmpegPath, movieOutputDir);

                try {
                    ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-version");
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    log.info("FFmpeg version check exit code: {}", exitCode);
                } catch (Exception e) {
                    log.error("Không thể kiểm tra phiên bản FFmpeg: {}", e.getMessage());
                }
                
                // Chuyển đổi video sang HLS với nhiều bitrate
                log.info("Bắt đầu chuyển đổi video: {} sang HLS với outputName: {}", videoPath, episodeSlug);
                String masterPlaylist = converter.convertToAdaptiveHls(
                        videoPath, episodeSlug, 10);
                
                log.info("Hoàn thành chuyển đổi video, master playlist: {}", masterPlaylist);
                
                // Cập nhật đường dẫn video và trạng thái trong transaction riêng
                String videoUrl = "/api/videos/hls/" + movieId + "/" + episodeSlug + "/" + episodeSlug + ".m3u8";
                String hlsFolder = movieOutputDir + "/" + episodeSlug;
                
                updateEpisodeStatus(episodeId, "completed", videoUrl, hlsFolder);
                
                log.info("Đã xử lý video thành công cho tập phim ID: {}", episodeId);
            } catch (Exception e) {
                log.error("Lỗi khi xử lý video cho tập phim ID: " + episodeId, e);
                
                // Cập nhật trạng thái lỗi trong transaction riêng
                try {
                    updateEpisodeStatus(episodeId, "error", null, null);
                } catch (Exception ex) {
                    log.error("Không thể cập nhật trạng thái lỗi cho tập phim ID: {}", episodeId, ex);
                }
            }
        }).start();
    }
    
    /**
     * Xử lý video đồng bộ (chờ cho đến khi hoàn thành)
     * @param videoPath Đường dẫn đến file MP4
     * @param episodeId ID của tập phim
     * @return true nếu xử lý thành công, false nếu có lỗi
     */
    public boolean processVideoSync(String videoPath, Episode episode) {
        if(episode == null) {
            throw new BadRequestException("Tập phim không tồn tại");
        }
        try {
            log.info("Bắt đầu xử lý video cho tập phim ID: {}", episode.getId());
            
            // Cập nhật trạng thái đang xử lý
            episode.setStatus("processing");
            episodeRepository.save(episode);

            // Tạo cấu trúc thư mục phân cấp: movieId/episodeSlug
            Long movieId = episode.getMovie().getId();
            String episodeSlug = episode.getSlug();
            
            // Tạo thư mục đầu ra theo ID phim
            String movieOutputDir = hlsDir + "/" + movieId;
            Path movieDirPath = Paths.get(movieOutputDir);
            if (!Files.exists(movieDirPath)) {
                Files.createDirectories(movieDirPath);
            }
            
            // Thư mục cho tập phim cụ thể
            String episodeOutputDir = movieOutputDir + "/" + episodeSlug;
            Path episodeDirPath = Paths.get(episodeOutputDir);
            if (!Files.exists(episodeDirPath)) {
                Files.createDirectories(episodeDirPath);
            }

            // Khởi tạo converter với thư mục đích là thư mục của phim
            Mp4ToHlsConverter converter = new Mp4ToHlsConverter(ffmpegPath, movieOutputDir);
            
            // Chuyển đổi video sang HLS với nhiều bitrate
            String masterPlaylist = converter.convertToAdaptiveHls(
                    videoPath, episodeSlug, 10);
            
            // Cập nhật đường dẫn video và trạng thái
            episode.setVideoUrl("/api/videos/hls/" + movieId + "/" + episodeSlug + "/" + episodeSlug + ".m3u8");
            episode.setHlsFolder(movieOutputDir + "/" + episodeSlug);
            episode.setStatus("completed");
            episodeRepository.save(episode);
            
            log.info("Đã xử lý video thành công cho tập phim ID: {}", episode.getId());
            return true;
        } catch (Exception e) {
            log.error("Lỗi khi xử lý video cho tập phim ID: " + episode.getId(), e);
                
            // Cập nhật trạng thái lỗi
            episode.setStatus("error");
            episodeRepository.save(episode);
            return false;
        }
    }
}