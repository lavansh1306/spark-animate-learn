package com.srm.spark.controller;

import com.srm.spark.dto.ReplyRequest;
import com.srm.spark.dto.ReplyResponse;
import com.srm.spark.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<ReplyResponse>> getRepliesByQuestion(@PathVariable String questionId) {
        return ResponseEntity.ok(replyService.getRepliesByQuestion(questionId));
    }

    @PostMapping("/question/{questionId}")
    public ResponseEntity<ReplyResponse> createReply(
            @PathVariable String questionId,
            @Valid @RequestBody ReplyRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(replyService.createReply(questionId, request, userEmail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReplyResponse> updateReply(
            @PathVariable String id,
            @Valid @RequestBody ReplyRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(replyService.updateReply(id, request, userEmail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteReply(
            @PathVariable String id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        replyService.deleteReply(id, userEmail);
        return ResponseEntity.ok(Map.of("message", "Reply deleted successfully"));
    }
}
