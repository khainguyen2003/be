package com.datn.motchill.examples.videoProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Lớp tiện ích để chuyển đổi tệp video MP4 sang định dạng HLS sử dụng FFmpeg.
 * Lớp này yêu cầu FFmpeg được cài đặt trên hệ thống và có thể truy cập qua dòng lệnh.
 * 
 * Luồng xử lý chính:
 * 1. Nhận file MP4 đầu vào
 * 2. Tạo thư mục đầu ra cho các file HLS
 * 3. Phân tích video thành nhiều phiên bản chất lượng khác nhau (1080p, 720p, 480p)
 * 4. Tạo file playlist master (.m3u8) và các segment video (.ts)
 * 5. Trả về đường dẫn đến file playlist master
 */
public class Mp4ToHlsConverter {
    
    private final String ffmpegPath;
    private final String outputDir;
    
    /**
     * Creates a new MP4 to HLS converter
     * 
     * @param ffmpegPath The path to the FFmpeg executable
     * @param outputDir The directory where HLS files will be saved
     */
    public Mp4ToHlsConverter(String ffmpegPath, String outputDir) {
        this.ffmpegPath = ffmpegPath;
        this.outputDir = outputDir;
        
        // Create output directory if it doesn't exist
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Chuyển đổi một file MP4 sang định dạng HLS cơ bản (một chất lượng duy nhất)
     * 
     * Luồng xử lý:
     * 1. Kiểm tra tệp đầu vào có tồn tại không
     * 2. Tạo thư mục đầu ra cho video cụ thể
     * 3. Chạy lệnh FFmpeg để chuyển đổi MP4 sang HLS
     * 4. Đọc và ghi log output của FFmpeg
     * 5. Trả về đường dẫn đến file playlist master
     * 
     * @param inputFilePath Đường dẫn đến file MP4 đầu vào
     * @param outputFileName Tên cho các file đầu ra (không có phần mở rộng)
     * @param segmentDuration Thời lượng của mỗi segment HLS tính bằng giây
     * @return Đường dẫn đến file playlist master (.m3u8)
     * @throws IOException Nếu xảy ra lỗi trong quá trình chuyển đổi
     */
    public String convertToHls(String inputFilePath, String outputFileName, int segmentDuration) throws IOException {
        // Validate input file
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists() || !inputFile.isFile()) {
            throw new IOException("Input file does not exist: " + inputFilePath);
        }
        
        // Create output directory for this specific video
        String videoOutputDir = Paths.get(outputDir, outputFileName).toString();
        Files.createDirectories(Paths.get(videoOutputDir));
        
        // Create the master playlist path
        String masterPlaylistPath = Paths.get(videoOutputDir, outputFileName + ".m3u8").toString();
        
        // Build FFmpeg command for HLS conversion
        List<String> command = buildFfmpegCommand(inputFilePath, videoOutputDir, outputFileName, segmentDuration);
        
        // Execute the command
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        
        Process process = processBuilder.start();
        
        // Read the output (for logging purposes)
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        // Process FFmpeg output asynchronously
        CompletableFuture.runAsync(() -> {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        // Wait for the process to complete
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg process exited with code " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFmpeg process was interrupted", e);
        }
        
        return masterPlaylistPath;
    }
    
    /**
     * Builds the FFmpeg command for HLS conversion with multiple quality variants
     */
    private List<String> buildFfmpegCommand(String inputFile, String outputDir, String outputFileName, int segmentDuration) {
        List<String> command = new ArrayList<>();
        
        command.add(ffmpegPath);
        command.add("-i");
        command.add(inputFile);
        command.add("-profile:v");
        command.add("baseline");
        command.add("-level");
        command.add("3.0");
        command.add("-start_number");
        command.add("0");
        command.add("-hls_time");
        command.add(String.valueOf(segmentDuration));
        command.add("-hls_list_size");
        command.add("0");
        command.add("-f");
        command.add("hls");
        
        // Add multiple quality variants
        // 1080p variant (if source is high enough quality)
        command.add("-map");
        command.add("0:v");
        command.add("-map");
        command.add("0:a");
        command.add("-c:v");
        command.add("libx264");
        command.add("-c:a");
        command.add("aac");
        command.add("-b:v");
        command.add("5000k");
        command.add("-g");
        command.add("60");
        command.add("-var_stream_map");
        command.add("v:0,a:0");
        command.add("-master_pl_name");
        command.add(outputFileName + ".m3u8");
        command.add("-hls_segment_filename");
        command.add(Paths.get(outputDir, outputFileName + "_%03d.ts").toString());
        command.add(Paths.get(outputDir, outputFileName + "_playlist.m3u8").toString());
        
        return command;
    }
    
    /**
     * Tạo nhiều phiên bản video với các chất lượng khác nhau sử dụng filter_complex của FFmpeg
     * Phương thức này sử dụng filter_complex để tạo nhiều biến thể với độ phân giải/bitrate khác nhau
     * 
     * Luồng xử lý:
     * 1. Kiểm tra tệp đầu vào có tồn tại không
     * 2. Tạo thư mục đầu ra cho video và các thư mục con cho từng chất lượng
     * 3. Thiết lập lệnh FFmpeg với filter_complex để tạo 3 phiên bản:
     *    - 1080p (1920x1080) với bitrate 5000k
     *    - 720p (1280x720) với bitrate 2800k
     *    - 480p (854x480) với bitrate 1400k
     * 4. Cấu hình âm thanh cho mỗi phiên bản
     * 5. Tạo file master playlist và các segment .ts
     * 6. Thực hiện lệnh FFmpeg và theo dõi quá trình
     * 
     * @param inputFilePath Đường dẫn đến file MP4 đầu vào
     * @param outputFileName Tên cho các file đầu ra (không có phần mở rộng)
     * @param segmentDuration Thời lượng của mỗi segment HLS tính bằng giây
     * @return Đường dẫn đến file playlist master (.m3u8)
     * @throws IOException Nếu xảy ra lỗi trong quá trình chuyển đổi
     */
    public String convertToAdaptiveHls(String inputFilePath, String outputFileName, int segmentDuration) throws IOException {
        // Validate input file
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists() || !inputFile.isFile()) {
            throw new IOException("Input file does not exist: " + inputFilePath);
        }
        
        // Create output directory for this specific video
        String videoOutputDir = Paths.get(outputDir, outputFileName).toString();
        Files.createDirectories(Paths.get(videoOutputDir));
        
        // Create subdirectories for each quality variant
        for (int i = 0; i < 3; i++) {
            Files.createDirectories(Paths.get(videoOutputDir, "stream_" + i));
        }
        
        // Create the master playlist path với tên trùng với videoId
        String masterPlaylistPath = Paths.get(videoOutputDir, outputFileName + ".m3u8").toString();

        // Đầu tiên kiểm tra xem video có audio stream không
        boolean hasAudio = checkForAudioStream(inputFile);
        
        // Build the filter_complex command
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(inputFilePath);
        command.add("-filter_complex");
        command.add("[0:v]split=3[v1][v2][v3]; [v1]scale=w=1920:h=1080[v1out]; [v2]scale=w=1280:h=720[v2out]; [v3]scale=w=854:h=480[v3out]");
        
        // Map video streams with different qualities
        command.add("-map");
        command.add("[v1out]");
        command.add("-c:v:0");
        command.add("libx264");
        command.add("-b:v:0");
        command.add("5000k");
        command.add("-maxrate:v:0");
        command.add("5350k");
        command.add("-bufsize:v:0");
        command.add("7500k");
        
        command.add("-map");
        command.add("[v2out]");
        command.add("-c:v:1");
        command.add("libx264");
        command.add("-b:v:1");
        command.add("2800k");
        command.add("-maxrate:v:1");
        command.add("2996k");
        command.add("-bufsize:v:1");
        command.add("4200k");
        
        command.add("-map");
        command.add("[v3out]");
        command.add("-c:v:2");
        command.add("libx264");
        command.add("-b:v:2");
        command.add("1400k");
        command.add("-maxrate:v:2");
        command.add("1498k");
        command.add("-bufsize:v:2");
        command.add("2100k");
        
        // Chỉ thêm audio mapping nếu có audio stream
        if (hasAudio) {
            // Map audio streams
            command.add("-map");
            command.add("a:0");
            command.add("-c:a");
            command.add("aac");
            command.add("-b:a:0");
            command.add("192k");
            command.add("-ac");
            command.add("2");
            
            command.add("-map");
            command.add("a:0");
            command.add("-c:a");
            command.add("aac");
            command.add("-b:a:1");
            command.add("128k");
            command.add("-ac");
            command.add("2");
            
            command.add("-map");
            command.add("a:0");
            command.add("-c:a");
            command.add("aac");
            command.add("-b:a:2");
            command.add("96k");
            command.add("-ac");
            command.add("2");
        }
        
        // HLS output settings
        command.add("-f");
        command.add("hls");
        command.add("-hls_time");
        command.add(String.valueOf(segmentDuration));
        command.add("-hls_playlist_type");
        command.add("vod");
        command.add("-hls_flags");
        command.add("independent_segments");
        command.add("-hls_segment_type");
        command.add("mpegts");
        command.add("-hls_segment_filename");
        command.add(videoOutputDir + "/stream_%v/data%03d.ts");
        command.add("-master_pl_name");
        command.add(outputFileName + ".m3u8");
        command.add("-var_stream_map");
        // Thay đổi var_stream_map dựa trên việc có audio hay không
        if (hasAudio) {
            command.add("v:0,a:0 v:1,a:1 v:2,a:2");
        } else {
            command.add("v:0 v:1 v:2");
        }
        command.add(videoOutputDir + "/stream_%v/playlist.m3u8");
        
        // Execute the command
        System.out.println("Executing FFmpeg command: " + String.join(" ", command));
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        
        // Đọc output và error streams sử dụng StreamGobbler
        // FFmpeg thường đưa thông tin tiến trình qua stderr nhưng không phải là lỗi thực sự
        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "FFMPEG");
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
        errorGobbler.start();
        outputGobbler.start();
        
        // Wait for process to complete
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg process exited with code " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFmpeg process was interrupted", e);
        }
        
        return masterPlaylistPath;
    }

    /**
     * Kiểm tra xem file video có audio stream hay không
     * @param inputFile Đối tượng File cần kiểm tra
     * @return true nếu file có audio stream, false nếu không
     */
    public boolean checkForAudioStream(File inputFile) {
        try {
            List<String> command = new ArrayList<>();
            command.add(ffmpegPath);
            command.add("-i");
            command.add(inputFile.getAbsolutePath());
            command.add("-hide_banner");
            command.add("-loglevel");
            command.add("error");
            
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            
            // Đọc stderr để kiểm tra audio stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // Đợi tiến trình hoàn thành nhưng không sử dụng mã thoát
            process.waitFor();
            
            // Tìm kiếm audio stream trong output
            return output.toString().contains("Stream #") && output.toString().contains("Audio");
        } catch (Exception e) {
            System.err.println("Error checking for audio stream: " + e.getMessage());
            // Nếu không thể kiểm tra, giả định là không có audio để an toàn
            return false;
        }
    }

    private static class StreamGobbler extends Thread {
        private final InputStream inputStream;
        private final String type;

        public StreamGobbler(InputStream inputStream, String type) {
            this.inputStream = inputStream;
            this.type = type;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(type + "> " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
