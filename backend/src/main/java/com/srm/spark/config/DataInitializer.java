package com.srm.spark.config;

import com.srm.spark.model.Page;
import com.srm.spark.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PageRepository pageRepository;

    @Override
    public void run(String... args) {
        if (pageRepository.count() == 0) {
            log.info("Initializing default pages...");
            
            List<PageData> defaultPages = Arrays.asList(
                new PageData("CSE", "Computer Science and Engineering - Programming, Data Structures, Algorithms, DBMS, OS, Networks"),
                new PageData("ECE", "Electronics and Communication Engineering - Circuits, Signals, Communication Systems, VLSI"),
                new PageData("Mathematics", "Mathematics - Calculus, Linear Algebra, Probability, Statistics, Discrete Math"),
                new PageData("Physics", "Physics - Mechanics, Thermodynamics, Electromagnetism, Quantum Physics"),
                new PageData("AI/ML", "Artificial Intelligence and Machine Learning - Neural Networks, Deep Learning, NLP, Computer Vision"),
                new PageData("General", "General Doubts - Any other academic or non-academic questions")
            );

            for (PageData pageData : defaultPages) {
                if (!pageRepository.existsByName(pageData.name)) {
                    Page page = new Page();
                    page.setName(pageData.name);
                    page.setDescription(pageData.description);
                    pageRepository.save(page);
                    log.info("Created page: {}", pageData.name);
                }
            }
            
            log.info("Database initialized with default pages!");
        } else {
            log.info("Pages already exist in database. Skipping initialization.");
        }
    }

    private static class PageData {
        String name;
        String description;

        PageData(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
