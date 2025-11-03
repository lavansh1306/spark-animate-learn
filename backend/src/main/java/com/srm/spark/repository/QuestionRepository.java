package com.srm.spark.repository;

import com.srm.spark.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    
    @Query("SELECT q FROM Question q WHERE q.page.id = :pageId ORDER BY q.createdAt DESC")
    Page<Question> findByPageId(String pageId, Pageable pageable);
    
    @Query("SELECT q FROM Question q WHERE q.user.id = :userId ORDER BY q.createdAt DESC")
    List<Question> findByUserId(String userId);
    
    @Query("SELECT q FROM Question q WHERE q.page.name = :pageName ORDER BY q.createdAt DESC")
    Page<Question> findByPageName(String pageName, Pageable pageable);
}
