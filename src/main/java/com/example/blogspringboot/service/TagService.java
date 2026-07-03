package com.example.blogspringboot.service;

import com.example.blogspringboot.dto.article.ArticleResponse;
import com.example.blogspringboot.dto.tag.TagResponse;
import com.example.blogspringboot.dto.user.UserResponse;
import com.example.blogspringboot.entity.Article;
import com.example.blogspringboot.entity.Tag;
import com.example.blogspringboot.exception.ResourceNotFoundException;
import com.example.blogspringboot.repository.ArticleRepository;
import com.example.blogspringboot.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public List<TagResponse> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ArticleResponse> findArticlesByTagName(String tagName) {
        tagRepository.findByName(tagName)
                .orElseThrow(() -> new ResourceNotFoundException("Tag introuvable : " + tagName));

        return articleRepository.findByTagName(tagName)
                .stream()
                .map(this::toArticleResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag introuvable avec l'id " + id));
        tagRepository.delete(tag);
    }

    private TagResponse toResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    private ArticleResponse toArticleResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(UserResponse.builder()
                        .id(article.getAuthor().getId())
                        .email(article.getAuthor().getEmail())
                        .username(article.getAuthor().getUsername())
                        .createdAt(article.getAuthor().getCreatedAt())
                        .build())
                .tags(article.getTags().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toSet()))
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}