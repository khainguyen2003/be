package com.datn.motchill.examples.videoProcessing;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Example demonstrating how to use the Mp4ToHlsConverter
 * to convert MP4 video files to HLS format.
 */
public class Mp4ToHlsExample {
    
    public static void main(String[] args) {
        // Specify the path to FFmpeg executable
        // On Windows, it might be something like "C:\\path\\to\\ffmpeg.exe"
        // On Linux/Mac, it might be just "ffmpeg" if it's in your PATH
        String ffmpegPath = "ffmpeg";
        
        // Directory where the HLS files will be stored
        String outputDir = "output/hls";
        
        // Create the converter
        Mp4ToHlsConverter converter = new Mp4ToHlsConverter(ffmpegPath, outputDir);
        
        try {
            // Example 1: Basic conversion
            String inputFile = "videos/chu_thuat_hoi_chien/chu_thuat_hoi_chien.mp4";
            String outputName = "chu_thuat_hoi_chien";
            int segmentDuration = 10; // 10 seconds per segment
            
            System.out.println("Converting MP4 to basic HLS format...");
            String masterPlaylist = converter.convertToHls(inputFile, outputName, segmentDuration);
            System.out.println("Conversion complete! Master playlist available at: " + masterPlaylist);
            
            // Example 2: Adaptive bitrate streaming (multiple quality variants)
            String inputFile2 = "videos/chu_thuat_hoi_chien/chu_thuat_hoi_chien.mp4";
            String outputName2 = "adaptive_video";
            
            System.out.println("\nConverting MP4 to adaptive HLS format with multiple qualities...");
            String adaptiveMasterPlaylist = converter.convertToAdaptiveHls(inputFile2, outputName2, segmentDuration);
            System.out.println("Adaptive conversion complete! Master playlist available at: " + adaptiveMasterPlaylist);
            
        } catch (IOException e) {
            System.err.println("Error during conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * A more practical example showing how to use the converter in a web application context
     */
    public static String convertVideoForWebsite(String originalVideoPath, String videoId) {
        String ffmpegPath = "ffmpeg"; // Adjust based on your environment
        String hlsOutputDir = Paths.get("media", "hls").toString();
        
        Mp4ToHlsConverter converter = new Mp4ToHlsConverter(ffmpegPath, hlsOutputDir);
        
        try {
            // Create adaptive streaming with multiple quality variants
            // 10 second segments are good for most web streaming cases
            String masterPlaylistPath = converter.convertToAdaptiveHls(originalVideoPath, videoId, 10);
            
            // Return the relative URL that would be used to access this stream from a web browser
            return Paths.get("/media/hls", videoId, videoId + "_master.m3u8").toString().replace("\\", "/");
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
