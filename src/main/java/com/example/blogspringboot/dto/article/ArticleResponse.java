package com.example.blogspringboot.dto.article;

import com.example.blogspringboot.dto.tag.TagResponse;
import com.example.blogspringboot.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private UserResponse author;
    private Set<TagResponse> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}