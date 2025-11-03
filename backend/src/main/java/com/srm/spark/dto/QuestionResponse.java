package com.srm.spark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private String id;
    private String title;
    private String description;
    private String userId;
    private String userName;
    private String pageId;
    private String pageName;
    private int replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
