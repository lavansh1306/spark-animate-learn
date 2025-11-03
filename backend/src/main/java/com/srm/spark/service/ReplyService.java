package com.srm.spark.service;

import com.srm.spark.dto.ReplyRequest;
import com.srm.spark.dto.ReplyResponse;
import com.srm.spark.model.Question;
import com.srm.spark.model.Reply;
import com.srm.spark.model.User;
import com.srm.spark.repository.QuestionRepository;
import com.srm.spark.repository.ReplyRepository;
import com.srm.spark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public List<ReplyResponse> getRepliesByQuestion(String questionId) {
        return replyRepository.findByQuestionId(questionId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReplyResponse createReply(String questionId, ReplyRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        Reply reply = new Reply();
        reply.setContent(request.getContent());
        reply.setQuestion(question);
        reply.setUser(user);

        Reply savedReply = replyRepository.save(reply);
        return convertToResponse(savedReply);
    }

    @Transactional
    public ReplyResponse updateReply(String id, ReplyRequest request, String userEmail) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reply not found with id: " + id));

        if (!reply.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to update this reply");
        }

        reply.setContent(request.getContent());

        Reply updatedReply = replyRepository.save(reply);
        return convertToResponse(updatedReply);
    }

    @Transactional
    public void deleteReply(String id, String userEmail) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reply not found with id: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!reply.getUser().getEmail().equals(userEmail) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You are not authorized to delete this reply");
        }

        replyRepository.deleteById(id);
    }

    private ReplyResponse convertToResponse(Reply reply) {
        return new ReplyResponse(
                reply.getId(),
                reply.getContent(),
                reply.getQuestion().getId(),
                reply.getUser().getId(),
                reply.getUser().getName(),
                reply.getCreatedAt(),
                reply.getUpdatedAt()
        );
    }
}
