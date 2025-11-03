package com.srm.spark.service;

import com.srm.spark.dto.QuestionRequest;
import com.srm.spark.dto.QuestionResponse;
import com.srm.spark.model.Page;
import com.srm.spark.model.Question;
import com.srm.spark.model.User;
import com.srm.spark.repository.PageRepository;
import com.srm.spark.repository.QuestionRepository;
import com.srm.spark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final PageRepository pageRepository;

    public List<QuestionResponse> getQuestionsByPage(String pageId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.findByPageId(pageId, pageable)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getQuestionsByPageName(String pageName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.findByPageName(pageName, pageable)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return convertToResponse(question);
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page page = pageRepository.findById(request.getPageId())
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + request.getPageId()));

        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setDescription(request.getDescription());
        question.setUser(user);
        question.setPage(page);

        Question savedQuestion = questionRepository.save(question);
        return convertToResponse(savedQuestion);
    }

    @Transactional
    public QuestionResponse updateQuestion(String id, QuestionRequest request, String userEmail) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));

        if (!question.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to update this question");
        }

        question.setTitle(request.getTitle());
        question.setDescription(request.getDescription());

        Question updatedQuestion = questionRepository.save(question);
        return convertToResponse(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(String id, String userEmail) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!question.getUser().getEmail().equals(userEmail) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You are not authorized to delete this question");
        }

        questionRepository.deleteById(id);
    }

    private QuestionResponse convertToResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getDescription(),
                question.getUser().getId(),
                question.getUser().getName(),
                question.getPage().getId(),
                question.getPage().getName(),
                question.getReplies().size(),
                question.getCreatedAt(),
                question.getUpdatedAt()
        );
    }
}
