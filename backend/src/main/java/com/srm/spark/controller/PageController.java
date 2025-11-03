package com.srm.spark.controller;

import com.srm.spark.dto.PageResponse;
import com.srm.spark.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping
    public ResponseEntity<List<PageResponse>> getAllPages() {
        return ResponseEntity.ok(pageService.getAllPages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PageResponse> getPageById(@PathVariable String id) {
        return ResponseEntity.ok(pageService.getPageById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PageResponse> getPageByName(@PathVariable String name) {
        return ResponseEntity.ok(pageService.getPageByName(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse> createPage(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        return ResponseEntity.ok(pageService.createPage(name, description));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deletePage(@PathVariable String id) {
        pageService.deletePage(id);
        return ResponseEntity.ok(Map.of("message", "Page deleted successfully"));
    }
}
