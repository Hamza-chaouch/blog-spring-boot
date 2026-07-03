package com.example.blogspringboot.service;

import com.example.blogspringboot.dto.article.ArticleRequest;
import com.example.blogspringboot.dto.article.ArticleResponse;
import com.example.blogspringboot.dto.tag.TagResponse;
import com.example.blogspringboot.dto.user.UserResponse;
import com.example.blogspringboot.entity.Article;
import com.example.blogspringboot.entity.Tag;
import com.example.blogspringboot.entity.User;
import com.example.blogspringboot.exception.ForbiddenException;
import com.example.blogspringboot.exception.ResourceNotFoundException;
import com.example.blogspringboot.repository.ArticleRepository;
import com.example.blogspringboot.repository.TagRepository;
import com.example.blogspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public List<ArticleResponse> findAll() {
        return articleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ArticleResponse findById(Long id) {
        Article article = getArticleOrThrow(id);
        return toResponse(article);
    }

    public ArticleResponse create(ArticleRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id " + authorId));

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .tags(resolveTags(request.getTags()))
                .build();

        Article saved = articleRepository.save(article);
        return toResponse(saved);
    }

    public ArticleResponse update(Long id, ArticleRequest request, Long currentUserId) {
        Article article = getArticleOrThrow(id);
        checkOwnership(article, currentUserId);

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setTags(resolveTags(request.getTags()));

        Article updated = articleRepository.save(article);
        return toResponse(updated);
    }

    public void delete(Long id, Long currentUserId) {
        Article article = getArticleOrThrow(id);
        checkOwnership(article, currentUserId);
        articleRepository.delete(article);
    }

    private void checkOwnership(Article article, Long currentUserId) {
        if (!article.getAuthor().getId().equals(currentUserId)) {
            throw new ForbiddenException("Vous n'êtes pas autorisé à modifier cet article");
        }
    }

    // --- Helpers ---

    private Article getArticleOrThrow(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable avec l'id " + id));
    }

    private Set<Tag> resolveTags(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return Set.of();
        }
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build())))
                .collect(Collectors.toSet());
    }

    private ArticleResponse toResponse(Article article) {
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
                        .map(tag -> TagResponse.builder().id(tag.getId()).name(tag.getName()).build())
                        .collect(Collectors.toSet()))
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}