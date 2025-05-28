package com.datn.motchill.service;

import com.datn.motchill.admin.presentation.dto.episodes.EpisodeResponseDTO;
import com.datn.motchill.dto.EpisodeDto;
import com.datn.motchill.entity.Episode;
import com.datn.motchill.entity.Movie;
import com.datn.motchill.repository.EpisodeRepository;
import com.datn.motchill.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final MovieRepository movieRepository;

    private final ObjectMapper objectMapper;

    private final String dataFolder = "videos";

    public EpisodeDto saveEpisode(MultipartFile file, Long movieId, String name) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Integer episodeNumber = episodeRepository.countByMovieId(movieId);

        // Lưu tệp video
        Path storageFolder = Paths.get(dataFolder + "/" + movie.getId());
        if (!Files.exists(storageFolder)) {
            Files.createDirectories(storageFolder);
        }

        try {
            // Lưu file gốc
            String slug;
            if(StringUtils.hasText(name)) {
                name = name.trim();
                slug = generateSlug(name);
            } else {
                name = String.valueOf(episodeNumber);
                slug = "tap-" + episodeNumber;
            }

            String originalFilename = StringUtils.cleanPath(slug + "." + getFileExtension(file));
            Path targetLocation = storageFolder.resolve(originalFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);



            // TODO: Chạy ffmpeg để chuyển đổi sang .m3u8
            // Giả sử đầu ra ở thư mục videos/m3u8/{episodeSlug}/index.m3u8
            // Bạn có thể chạy lệnh ffmpeg bằng ProcessBuilder hoặc service riêng.

            // Ví dụ (bạn cần cài ffmpeg trên server):
//            String outputDir = storageFolder.resolve("m3u8").resolve(slug).toString();
//            String filePath = outputDir + "/index.m3u8";
//            Files.createDirectories(Paths.get(outputDir));
//
//            ProcessBuilder pb = new ProcessBuilder("ffmpeg",
//                    "-i", targetLocation.toString(),
//                    "-codec:", "copy",
//                    "-start_number", "0",
//                    "-hls_time", "10",
//                    "-hls_list_size", "0",
//                    "-f", "hls",
//                    outputDir + "/index.m3u8");
//            pb.redirectErrorStream(true);
//            Process process = pb.start();
//
//            // Đọc output của ffmpeg (bạn có thể lưu log)
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//            }
//            int exitCode = process.waitFor();
//            if (exitCode != 0) {
//                throw new RuntimeException("Lỗi chuyển đổi video sang m3u8");
//            }

//            Path directory = Path.of(dataFolder, slug);
//            Files.createDirectory(directory);
//            Path filePath = Path.of(directory.toString(), file.getOriginalFilename());
//            try (OutputStream output = Files.newOutputStream(filePath, CREATE, WRITE)) {
//                file.getInputStream().transferTo(output);
//            }
//
//            String fileName = slug + ".mp4";
//            Path destinationFile = storageFolder.resolve(Paths.get(fileName))
//                    .normalize().toAbsolutePath();

            // Lưu file
//            try (InputStream inputStream = file.getInputStream()) {
//                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
//            }

            // Tạo ảnh preview
//            long videoLength = frameGrabberService.generatePreviewPictures(file);
//            metadata.setVideoLength(videoLength);
//            videoRepo.save(metadata);

            Episode episode = new Episode();
            episode.setName(name);
            episode.setSlug(slug);
            episode.setEpisodeNumber(episodeNumber);
            episode.setMovie(movie);
            episode.setVideoPath(targetLocation.toString());
            // episode.setThumbnailPath(thumbnailPath); // nếu có

            episode = episodeRepository.save(episode);

            // TODO: Lưu thông tin tập phim vào DB (tên, slug, embed, đường dẫn m3u8, ...)

            return objectMapper.convertValue(episode, EpisodeDto.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        List<Episode> episodes = episodeRepository.findByMovieId(movieId);

        // Xóa file video từng tập
        for (Episode episode : episodes) {
            try {
                if (episode.getVideoPath() != null) {
                    Path videoPath = Paths.get(episode.getVideoPath());
                    Files.deleteIfExists(videoPath); // Xóa file .mp4
                }

                // Nếu có thêm thư mục HLS (m3u8), xóa cả thư mục
                if (episode.getHlsFolder() != null) {
                    Path hlsFolder = Paths.get(episode.getHlsFolder());
                    if (Files.exists(hlsFolder)) {
                        deleteDirectoryRecursively(hlsFolder); // Xóa cả thư mục m3u8
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
                // Có thể log lỗi, không throw để tiếp tục xóa các tập khác
            }
        }

        // Xóa thư mục chứa video theo movieId (nếu không còn file nào)
        Path movieFolder = Paths.get("videos", String.valueOf(movieId));
        try {
            if (Files.exists(movieFolder)) {
                deleteDirectoryRecursively(movieFolder);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Xóa các tập và phim
        episodeRepository.deleteAll(episodes);
        movieRepository.delete(movie);
    }

    public void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                for (Path entry : entries.toList()) {
                    deleteDirectoryRecursively(entry);
                }
            }
        }
        Files.deleteIfExists(path);
    }

    private String generateSlug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex == -1) {
            return ""; // Không có đuôi
        }

        return originalFilename.substring(dotIndex + 1); // Ví dụ: "mp4", "mkv"
    }
}
