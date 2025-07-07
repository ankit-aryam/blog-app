package com.example.blog.dto;

import com.example.blog.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse {
    private long id;
    private String title;
    private String content;
    private String authorName;
    private String createdAt;
}
