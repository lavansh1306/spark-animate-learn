package com.srm.spark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReplyRequest {
    
    @NotBlank(message = "Content is required")
    @Size(min = 1, message = "Content must be at least 1 character")
    private String content;
}
