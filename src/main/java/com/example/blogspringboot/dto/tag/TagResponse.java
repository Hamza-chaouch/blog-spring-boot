package com.example.blogspringboot.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TagResponse {
    private Long id;
    private String name;
}