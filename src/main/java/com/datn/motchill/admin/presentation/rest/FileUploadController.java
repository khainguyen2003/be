package com.datn.motchill.admin.presentation.rest;

import com.datn.motchill.admin.domain.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("upload");
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadChunk(
            @RequestPart("file") MultipartFile file,
            @RequestParam("dzuuid") String uuid,
            @RequestParam("dzchunkindex") int chunkIndex,
            @RequestParam("dztotalchunkcount") int totalChunks,
            @RequestParam("name") String filename) {
        try {
            if (chunkIndex == 0 && totalChunks == 1) {
                // Single file upload or first chunk
                String uniqueFilename = fileStorageService.saveFile(file, filename);
                logger.info("Upload successful, filename: {}", uniqueFilename);
                return ResponseEntity.ok(uniqueFilename);
            } else {
                // If chunking is needed, handle it (simplified for now)
                String uniqueFilename = uuid + "_" + filename;
                fileStorageService.saveFile(file, uniqueFilename);
                logger.info("Chunk {} uploaded for file: {}", chunkIndex, uniqueFilename);
                return ResponseEntity.ok(uniqueFilename);
            }
        } catch (IOException e) {
            logger.error("Upload failed: {}", e.getMessage());
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
}
