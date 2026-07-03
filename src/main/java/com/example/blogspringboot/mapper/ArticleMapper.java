package com.example.blogspringboot.mapper;

import com.example.blogspringboot.dto.article.ArticleResponse;
import com.example.blogspringboot.dto.tag.TagResponse;
import com.example.blogspringboot.dto.user.UserResponse;
import com.example.blogspringboot.entity.Article;
import com.example.blogspringboot.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(toUserResponse(article))
                .tags(article.getTags().stream()
                        .map(this::toTagResponse)
                        .collect(Collectors.toSet()))
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private UserResponse toUserResponse(Article article) {
        return UserResponse.builder()
                .id(article.getAuthor().getId())
                .email(article.getAuthor().getEmail())
                .username(article.getAuthor().getUsername())
                .createdAt(article.getAuthor().getCreatedAt())
                .build();
    }

    private TagResponse toTagResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}