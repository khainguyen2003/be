package com.datn.motchill.service;

import com.datn.motchill.common.exceptions.BadRequestException;
import com.datn.motchill.dto.UploadResponseDTO;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private ResourceLoader resourceLoader;

    private String uploadDir = "upload/videos";
    private final String rootPath = "src\\main\\resources\\static";
    private final Path storageFileFolder = Paths.get(rootPath);

    private static final String videoDirPath = "D:\\learning\\datn\\motchill\\be\\upload\\videos";
    private static final String outputDirPath = "D:\\learning\\datn\\motchill\\be\\upload\\hls";

    private final Map<String, FileOutputStream> activeUploads = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            Files.createDirectories(storageFileFolder);
        } catch (IOException e) {
            throw new RuntimeException("Khong the khoi tao folder luu tru", e);
        }

    }

    // check file is image
    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] { "png", "jpg", "jpeg", "bmp" }).contains(fileExtension.trim().toLowerCase());
    }

    public String storeFile(MultipartFile file, String folderContent) {
        try {
            Path pathUploadFolder = Files.createDirectories(Paths.get(rootPath + "\\" +folderContent));
            // check empty
            if (file.isEmpty())
                throw new RuntimeException("File rong khong the luu tru");
            // check file
            if (!isImageFile(file))
                throw new RuntimeException("File khong dung dinh dang theo kieu image");
            // check size
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if (fileSizeInMegabytes > 5.0f)
                throw new RuntimeException("Dung luong file <= 5Mb");
            /*
             * rename file - client upload file len server thi phai doi lai ten file tren
             * server + neu user do tiep tuc upload file trung ten len server nua -> file cu
             * se bi ghi de + neu user khac upload 1 file co trung ten 1 user khac da upload
             * roi -> ca 2 user se cung 1 file -> ro ri du lieu
             */
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = pathUploadFolder.resolve(Paths.get(generatedFileName)).normalize()
                    .toAbsolutePath();
            if (!destinationFilePath.getParent().equals(pathUploadFolder.toAbsolutePath()))
                throw new RuntimeException("Khong the luu tru file ben ngoai thu muc hien hanh");
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/" + folderContent + "/" + generatedFileName;
        } catch (IOException e) {
            throw new RuntimeException("Loi luu tru file", e);
        }
    }

    public Stream<Path> loadAll() {
        try {
            // load all file in storageFileFolder
            return Files.walk(this.storageFileFolder, 1)
                    .filter(path -> !path.equals(this.storageFileFolder))
                    .map(this.storageFileFolder::relativize);
        }catch(IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFileFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException("Khong the doc file "+fileName);
            }
        }catch(IOException e) {
            throw new RuntimeException("Khong the doc file "+fileName);
        }
    }

    public String startUpload(String filename, byte[] chunk, int chunkNumber, int totalChunks) throws IOException {
        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        String filePath = Paths.get(uploadDir, uniqueFilename).toString();
        FileOutputStream fos = new FileOutputStream(filePath, chunkNumber > 0);
        activeUploads.put(uniqueFilename, fos);
        fos.write(chunk);
        fos.flush();
        if (chunkNumber + 1 == totalChunks) {
            fos.close();
            activeUploads.remove(uniqueFilename);
        }
        return uniqueFilename;
    }

    public void appendChunk(String uniqueFilename, byte[] chunk, int chunkNumber, int totalChunks) throws IOException {
        FileOutputStream fos = activeUploads.get(uniqueFilename);
        if (fos == null) {
            throw new IllegalStateException("Upload session not found for " + uniqueFilename);
        }
        fos.write(chunk);
        fos.flush();
        if (chunkNumber + 1 == totalChunks) {
            fos.close();
            activeUploads.remove(uniqueFilename);
        }
    }

    /**
     * Lưu file từ MultipartFile vào hệ thống
     * @param file File cần lưu
     * @param filename Tên file
     * @return Tên file duy nhất đã được lưu
     * @throws IOException Nếu có lỗi khi lưu file
     */
    public String saveFile(MultipartFile file, String filename) throws IOException {
        // Tạo tên file duy nhất để tránh trùng lặp
        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        
        // Tạo đường dẫn đầy đủ đến file
        Path filePath = Paths.get(uploadDir, uniqueFilename);
        
        // Đảm bảo thư mục tồn tại
        Files.createDirectories(filePath.getParent());
        
        // Lưu file
        file.transferTo(filePath.toFile());
        
        return uniqueFilename;
    }

    public UploadResponseDTO uploadVideo(MultipartFile file) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(Paths.get(videoDirPath));
            Files.createDirectories(Paths.get(outputDirPath));

            // Lưu video gốc
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new BadRequestException("Tên file không hợp lệ");
            }

            Path videoPath = Paths.get(videoDirPath, originalFileName);
            Files.write(videoPath, file.getBytes());

            // Tạo thư mục HLS output
            String outputFileName = UUID.randomUUID() + "_hls";
            Path hlsFolderPath = Paths.get(outputDirPath, outputFileName);
            Files.createDirectories(hlsFolderPath);

            Path m3u8Path = hlsFolderPath.resolve("index.m3u8");

            // Gọi FFmpeg để convert
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-i", videoPath.toString(),
                    "-c:v", "libx264",
                    "-c:a", "aac",
                    "-f", "hls",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-hls_segment_filename", hlsFolderPath.resolve("segment%d.ts").toString(),
                    m3u8Path.toString()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Log output FFmpeg
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg conversion failed with exit code " + exitCode);
            }

            // Trả kết quả về frontend
            UploadResponseDTO res = new UploadResponseDTO();
            res.setM3u8("/hls/" + outputFileName + "/index.m3u8"); // URL public
            res.setSourceUrl("/videos/" + originalFileName);       // URL public
            res.setHlsFolder(outputFileName);                      // Tên thư mục HLS nội bộ

            return res;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Upload or conversion failed", e);
        }
    }

    public void deleteHlsFolder(String hlsFolder) {
        if (StringUtils.hasText(hlsFolder)) {
            try {
                Path hlsPath = Paths.get(outputDirPath, hlsFolder); // không dùng resourceLoader nữa
                FileSystemUtils.deleteRecursively(hlsPath);
            } catch (IOException e) {
                throw new RuntimeException("Không thể xóa thư mục HLS: " + hlsFolder, e);
            }
        } else {
            throw new BadRequestException("Vui lòng nhập đường dẫn");
        }
    }

    public void deleteFile(String path) {
        try {
            Path videoPath = Paths.get(path);
            if (Files.exists(videoPath)) {
                Files.delete(videoPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa video gốc: " + path, e);
        }
    }

    public void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                entries.forEach(entry -> {
                    try {
                        deleteDirectoryRecursively(entry);
                    } catch (IOException e) {
                        throw new RuntimeException("Lỗi khi xóa thư mục: " + entry.toString(), e);
                    }
                });
            }
        }
        Files.deleteIfExists(path);
    }
}