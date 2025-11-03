package com.srm.spark.repository;

import com.srm.spark.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {
    
    @Query("SELECT r FROM Reply r WHERE r.question.id = :questionId ORDER BY r.createdAt ASC")
    List<Reply> findByQuestionId(String questionId);
    
    @Query("SELECT r FROM Reply r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Reply> findByUserId(String userId);
}
