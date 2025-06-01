//package com.datn.motchill.controller;
//
//import com.datn.motchill.dto.viewhistory.ViewHistoryDto;
//import com.datn.motchill.dto.viewhistory.ViewHistoryRequest;
//import com.datn.motchill.service.ViewHistoryService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/view-history")
//@RequiredArgsConstructor
//public class ViewHistoryController {
//
//    private final ViewHistoryService viewHistoryService;
//
//    @GetMapping
//    public ResponseEntity<Page<ViewHistoryDto>> getCurrentUserViewHistory(Pageable pageable) {
//        Long userId = getCurrentUserId();
//        return ResponseEntity.ok(viewHistoryService.findByUserId(userId, pageable));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ViewHistoryDto> getViewHistoryById(@PathVariable Long id) {
//        Long userId = getCurrentUserId();
//        ViewHistoryDto viewHistory = viewHistoryService.findById(id);
//
//        // Chỉ cho phép người dùng xem lịch sử của chính họ
//        if (!viewHistory.getUserId().equals(userId)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(viewHistory);
//    }
//
//    @GetMapping("/movie/{movieId}")
//    public ResponseEntity<ViewHistoryDto> getViewHistoryByMovie(@PathVariable Long movieId) {
//        Long userId = getCurrentUserId();
//        Optional<ViewHistoryDto> viewHistory = viewHistoryService.findByUserIdAndMovieId(userId, movieId);
//        return viewHistory.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/movie/{movieId}/episode/{episodeId}")
//    public ResponseEntity<ViewHistoryDto> getViewHistoryByMovieAndEpisode(
//            @PathVariable Long movieId,
//            @PathVariable Long episodeId) {
//        Long userId = getCurrentUserId();
//        Optional<ViewHistoryDto> viewHistory = viewHistoryService.findByUserIdAndMovieIdAndEpisodeId(userId, movieId, episodeId);
//        return viewHistory.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/record")
//    public ResponseEntity<ViewHistoryDto> recordView(@Valid @RequestBody ViewHistoryRequest request) {
//        Long userId = getCurrentUserId();
//        return ResponseEntity.ok(viewHistoryService.recordView(userId, request));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteViewHistory(@PathVariable Long id) {
//        Long userId = getCurrentUserId();
//        ViewHistoryDto viewHistory = viewHistoryService.findById(id);
//
//        // Chỉ cho phép người dùng xóa lịch sử của chính họ
//        if (!viewHistory.getUserId().equals(userId)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        viewHistoryService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Void> deleteAllViewHistory() {
//        Long userId = getCurrentUserId();
//        viewHistoryService.deleteAllByUserId(userId);
//        return ResponseEntity.noContent().build();
//    }
//
//    private Long getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return Long.parseLong(authentication.getName());
//    }
//}
