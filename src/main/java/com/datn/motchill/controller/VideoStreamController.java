package com.datn.motchill.controller;

import com.datn.motchill.entity.Episode;
import com.datn.motchill.examples.videoProcessing.Mp4ToHlsConverter;
import com.datn.motchill.repository.EpisodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller xử lý các yêu cầu liên quan đến streaming video
 * 
 * Luồng xử lý chính:
 * 1. Nhận yêu cầu URL stream từ frontend
 * 2. Tìm và chuyển đổi video MP4 sang HLS (nếu chưa có)
 * 3. Trả về URL HLS cho frontend
 * 4. Phục vụ các file HLS (.m3u8 và .ts) khi được yêu cầu
 */
@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "http://localhost:4200")  // Chỉ cho phép CORS từ Angular frontend
public class VideoStreamController {
    private static final Logger logger = LoggerFactory.getLogger(VideoStreamController.class);

    private final String uploadDir;
    private final String hlsDir;
    private final String ffmpegPath;
    private final Map<String, String> videoHlsCache;
    private final EpisodeRepository episodeRepository;

    public VideoStreamController(
            @Value("${app.upload.dir:./uploads}") String uploadDir,
            @Value("${app.hls.dir:./hls}") String hlsDir,
            @Value("${app.ffmpeg.path:ffmpeg}") String ffmpegPath,
            EpisodeRepository episodeRepository) {
        this.uploadDir = uploadDir;
        this.hlsDir = hlsDir;
        this.ffmpegPath = ffmpegPath;
        this.episodeRepository = episodeRepository;
        this.videoHlsCache = new ConcurrentHashMap<>();

        // Đảm bảo thư mục tồn tại
        createDirectoryIfNotExists(uploadDir);
        createDirectoryIfNotExists(hlsDir);
    }

    /**
     * API endpoint để lấy URL HLS của video dựa vào ID tập phim
     * 
     * Luồng xử lý:
     * 1. Nhận yêu cầu với episodeId
     * 2. Tìm tập phim trong database
     * 3. Kiểm tra và trả về URL HLS cho frontend
     */
    @GetMapping("/episode/{episodeId}/stream")
    public ResponseEntity<?> getEpisodeStream(@PathVariable Long episodeId) {
        try {
            logger.info("Yêu cầu stream cho episodeId: {}", episodeId);
            
            // Tìm thông tin tập phim trong database
            Optional<Episode> episodeOpt = episodeRepository.findById(episodeId);
            
            if (episodeOpt.isEmpty()) {
                logger.warn("Không tìm thấy tập phim với ID: {}", episodeId);
                return ResponseEntity.notFound().build();
            }
            
            Episode episode = episodeOpt.get();
            String videoUrl = episode.getVideoUrl();
            String videoStatus = episode.getStatus(); // Sử dụng trường status thay cho videoStatus
            
            // Kiểm tra trạng thái video
            if (videoUrl == null || videoUrl.isEmpty()) {
                logger.warn("Tập phim {} chưa có video", episodeId);
                Map<String, String> response = new HashMap<>();
                response.put("error", "Tập phim chưa có video");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            if ("processing".equals(videoStatus) || "uploaded".equals(videoStatus)) {
                logger.info("Video của tập phim {} đang được xử lý", episodeId);
                Map<String, String> response = new HashMap<>();
                response.put("status", "processing");
                response.put("message", "Video đang được xử lý");
                return ResponseEntity.accepted().body(response);
            }
            
            if ("ready".equals(videoStatus)) {
                logger.info("Trả về URL HLS cho tập phim: {}, URL: {}", episodeId, videoUrl);
                Map<String, String> response = new HashMap<>();
                response.put("hlsUrl", videoUrl);
                return ResponseEntity.ok(response);
            }
            
            logger.warn("Trạng thái video không hợp lệ: {}", videoStatus);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Trạng thái video không hợp lệ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            
        } catch (Exception e) {
            logger.error("Lỗi khi xử lý yêu cầu stream video: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Không thể xử lý video: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * API endpoint để lấy URL HLS của video (endpoint cũ cho tương thích ngược)
     */
    @GetMapping("/{videoId}/stream")
    public ResponseEntity<?> getHlsUrl(@PathVariable String videoId) {
        try {
            // Kiểm tra cache
            String hlsUrl = videoHlsCache.get(videoId);
            
            // Nếu chưa có trong cache, chuyển đổi video sang HLS
            if (hlsUrl == null) {
                // Kiểm tra xem có file HLS có sẵn không
                File existingHls = new File("output/hls/adaptive_video/master.m3u8");
                if (existingHls.exists() && "chu_thuat_hoi_chien".equals(videoId)) {
                    hlsUrl = "/api/videos/existing-hls/master.m3u8";
                } else {
                    hlsUrl = convertVideoToHls(videoId);
                }
                videoHlsCache.put(videoId, hlsUrl);
            }
            
            // Trả về URL cho trình phát
            Map<String, String> response = new HashMap<>();
            response.put("hlsUrl", hlsUrl);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Không thể xử lý video: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * API endpoint để phục vụ file HLS có sẵn từ thư mục output/hls/adaptive_video
     */
    @GetMapping("/existing-hls/**")
    public ResponseEntity<Resource> serveExistingHlsContent(HttpServletRequest request) {
        try {
            String requestPath = request.getRequestURI();
            String hlsPath = requestPath.substring("/api/videos/existing-hls/".length());
            
            Path filePath = Paths.get("output/hls/adaptive_video").resolve(hlsPath).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            String contentType = determineContentType(filePath.toString());
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * API endpoint để phục vụ các file HLS (.m3u8 và .ts)
     * 
     * Luồng xử lý:
     * 1. Nhận yêu cầu đến file HLS cụ thể (playlist .m3u8 hoặc segment .ts)
     * 2. Trích xuất đường dẫn tương đối từ URL
     * 3. Xây dựng đường dẫn đến file trên hệ thống
     * 4. Kiểm tra file có tồn tại không
     * 5. Xác định loại MIME dựa vào loại file
     * 6. Trả về file với các header CORS để cho phép frontend truy cập
     */
    @GetMapping("/hls/**")
    public ResponseEntity<Resource> serveHlsContent(
            @RequestParam(required = false) String videoId,
            HttpServletRequest request) {
        
        try {
            String requestPath = request.getRequestURI();
            // Trích xuất phần đường dẫn sau /api/videos/hls/
            String hlsPath = requestPath.substring("/api/videos/hls/".length());
            
            // Log đường dẫn để debug
            logger.info("Requested HLS path: {}", hlsPath);
            logger.info("Full HLS dir: {}", hlsDir);
            
            // Xây dựng đường dẫn tới file HLS
            // Cấu trúc thư mục hiện tại là: hlsDir/movieId/episodeSlug/episodeSlug.m3u8 hoặc segmentXXX.ts
            Path filePath = Paths.get(hlsDir).resolve(hlsPath).normalize();
            logger.info("Resolved file path: {}", filePath.toAbsolutePath());
            
            // Kiểm tra file tồn tại
            if (!Files.exists(filePath)) {
                logger.warn("File not found: {}", filePath.toAbsolutePath());
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            
            // Xác định loại MIME
            String contentType = determineContentType(filePath.toString());
            logger.info("Content type: {}", contentType);
            
            // Trả về file với header phù hợp
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(resource);
                    
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * API endpoint để lấy thông tin chi tiết của video
     */
    @GetMapping("/{videoId}")
    public ResponseEntity<?> getVideoDetails(@PathVariable String videoId) {
        try {
            // Tìm file video gốc
            File videoFile = findVideoFile(videoId);
            
            if (videoFile == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Tạo thông tin video
            Map<String, Object> videoDetails = new HashMap<>();
            videoDetails.put("id", videoId);
            videoDetails.put("title", videoFile.getName());
            videoDetails.put("duration", getVideoDurationInSeconds(videoFile));
            videoDetails.put("size", videoFile.length());
            videoDetails.put("createdAt", Files.getLastModifiedTime(videoFile.toPath()).toString());
            
            return ResponseEntity.ok(videoDetails);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Không thể lấy thông tin video: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Chuyển đổi video từ MP4 sang HLS
     * 
     * Luồng xử lý:
     * 1. Tìm file video MP4 dựa trên videoId
     * 2. Kiểm tra file có tồn tại không
     * 3. Tạo đường dẫn đầu ra cho các file HLS
     * 4. Sử dụng Mp4ToHlsConverter để chuyển đổi sang HLS đa chất lượng (adaptive)
     * 5. Trả về đường dẫn URL tương đối đến file master playlist (.m3u8)
     * 
     * @param videoId ID của video cần chuyển đổi
     * @return Đường dẫn URL tương đối đến file master playlist
     * @throws IOException Nếu xảy ra lỗi trong quá trình chuyển đổi
     */
    private String convertVideoToHls(String videoId) throws IOException {
        // Tìm file video MP4
        File videoFile = findVideoFile(videoId);
        
        if (videoFile == null) {
            throw new IOException("Không tìm thấy file video với ID: " + videoId);
        }
        
        // Chuẩn bị đường dẫn đầu ra cho HLS theo cấu trúc thư mục mới
        // uploads/videos/output/hls/adaptive_video/{movieId}/{episodeSlug}
        String outputDir = hlsDir + "/" + videoId;
        Path outputPath = Paths.get(outputDir);
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
        }
        
        logger.info("Chuẩn bị chuyển đổi video: {} với outputDir: {}", videoFile.getAbsolutePath(), outputDir);
        
        // Khởi tạo converter
        Mp4ToHlsConverter converter = new Mp4ToHlsConverter(ffmpegPath, outputDir);
        
        // Chuyển đổi MP4 sang HLS
        String videoPath = videoFile.getAbsolutePath();
        String masterPlaylist = converter.convertToAdaptiveHls(videoPath, videoId, 10);
        logger.info("Chuyển đổi thành công, master playlist: {}", masterPlaylist);
        
        // Trả về đường dẫn tương đối đến master playlist
        return "/api/videos/hls/" + videoId + "/" + videoId + ".m3u8";
    }
    
    /**
     * Tìm file video dựa trên ID
     */
    private File findVideoFile(String videoId) {
        File uploadDirectory = new File(uploadDir);
        
        // Tìm trong thư mục gốc
        File directVideoFile = new File(uploadDirectory, videoId + ".mp4");
        if (directVideoFile.exists() && directVideoFile.isFile()) {
            return directVideoFile;
        }
        
        // Tìm trong thư mục con
        File subdirectory = new File(uploadDirectory, videoId);
        if (subdirectory.exists() && subdirectory.isDirectory()) {
            File videoInSubdir = new File(subdirectory, videoId + ".mp4");
            if (videoInSubdir.exists() && videoInSubdir.isFile()) {
                return videoInSubdir;
            }
        }
        
        return null;
    }
    
    /**
     * Xác định loại MIME dựa trên đuôi file
     */
    private String determineContentType(String filePath) {
        if (filePath.endsWith(".m3u8")) {
            return "application/vnd.apple.mpegurl";
        } else if (filePath.endsWith(".ts")) {
            return "video/mp2t";
        } else {
            return "application/octet-stream";
        }
    }
    
    /**
     * Lấy thời lượng video (giây) - giả lập
     */
    private long getVideoDurationInSeconds(File videoFile) {
        // Trong thực tế, bạn cần sử dụng thư viện như JavaCV hoặc MediaInfo
        // Ở đây chúng ta giả lập dựa trên kích thước file
        return videoFile.length() / (3 * 1024 * 1024); // Giả sử 3MB/giây
    }
    
    /**
     * Tạo thư mục nếu chưa tồn tại
     */
    private void createDirectoryIfNotExists(String dir) {
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
