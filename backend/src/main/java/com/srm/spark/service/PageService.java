package com.srm.spark.service;

import com.srm.spark.dto.PageResponse;
import com.srm.spark.model.Page;
import com.srm.spark.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;

    public List<PageResponse> getAllPages() {
        return pageRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PageResponse getPageById(String id) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + id));
        return convertToResponse(page);
    }

    public PageResponse getPageByName(String name) {
        Page page = pageRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Page not found with name: " + name));
        return convertToResponse(page);
    }

    @Transactional
    public PageResponse createPage(String name, String description) {
        if (pageRepository.existsByName(name)) {
            throw new RuntimeException("Page already exists with name: " + name);
        }

        Page page = new Page();
        page.setName(name);
        page.setDescription(description);

        Page savedPage = pageRepository.save(page);
        return convertToResponse(savedPage);
    }

    @Transactional
    public void deletePage(String id) {
        if (!pageRepository.existsById(id)) {
            throw new RuntimeException("Page not found with id: " + id);
        }
        pageRepository.deleteById(id);
    }

    private PageResponse convertToResponse(Page page) {
        return new PageResponse(
                page.getId(),
                page.getName(),
                page.getDescription(),
                page.getQuestions().size(),
                page.getCreatedAt()
        );
    }
}
