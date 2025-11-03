package com.srm.spark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse {
    private String id;
    private String name;
    private String description;
    private int questionCount;
    private LocalDateTime createdAt;
}
