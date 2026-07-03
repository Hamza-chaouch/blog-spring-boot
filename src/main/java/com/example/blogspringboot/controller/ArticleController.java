package com.example.blogspringboot.controller;

import com.example.blogspringboot.dto.article.ArticleRequest;
import com.example.blogspringboot.dto.article.ArticleResponse;
import com.example.blogspringboot.security.SecurityUtils;
import com.example.blogspringboot.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll() {
        return ResponseEntity.ok(articleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        ArticleResponse created = articleService.create(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(articleService.update(id, request, currentUserId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        articleService.delete(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}