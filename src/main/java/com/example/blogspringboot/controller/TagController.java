package com.example.blogspringboot.controller;

import com.example.blogspringboot.dto.article.ArticleResponse;
import com.example.blogspringboot.dto.tag.TagResponse;
import com.example.blogspringboot.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> findAll() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @GetMapping("/{name}/articles")
    public ResponseEntity<List<ArticleResponse>> findArticlesByTag(@PathVariable String name) {
        return ResponseEntity.ok(tagService.findArticlesByTagName(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}