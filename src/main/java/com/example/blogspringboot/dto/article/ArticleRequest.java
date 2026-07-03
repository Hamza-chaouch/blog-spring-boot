package com.example.blogspringboot.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ArticleRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;

    private Set<String> tags;
}