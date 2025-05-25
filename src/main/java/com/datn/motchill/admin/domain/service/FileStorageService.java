package com.datn.motchill.admin.domain.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    private String uploadDir = "upload/videos";

    private final Map<String, FileOutputStream> activeUploads = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
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
}