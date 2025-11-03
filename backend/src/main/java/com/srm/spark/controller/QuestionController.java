package com.srm.spark.controller;

import com.srm.spark.dto.QuestionRequest;
import com.srm.spark.dto.QuestionResponse;
import com.srm.spark.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/page/{pageId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByPage(
            @PathVariable String pageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(questionService.getQuestionsByPage(pageId, page, size));
    }

    @GetMapping("/page/name/{pageName}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByPageName(
            @PathVariable String pageName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(questionService.getQuestionsByPageName(pageName, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable String id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(
            @Valid @RequestBody QuestionRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(questionService.createQuestion(request, userEmail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable String id,
            @Valid @RequestBody QuestionRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(questionService.updateQuestion(id, request, userEmail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteQuestion(
            @PathVariable String id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        questionService.deleteQuestion(id, userEmail);
        return ResponseEntity.ok(Map.of("message", "Question deleted successfully"));
    }
}
